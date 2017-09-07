package com.stone.app.dataBase;//package com.example.application.dataBase;
//
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DataBaseManager {
    private Realm Manager;

    // Only lazy deletion is safe, or ID conflict will occur
    private FamilyData AddFamily(String name, String memberID)
            throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            FamilyData newFamily = Manager.createObject(FamilyData.class);
            newFamily.setID(String.valueOf(Manager.where(FamilyData.class).findAll().size()));
            newFamily.setName(name);
            newFamily.setRootMemberID(memberID);
            newFamily.setActivate(true);
            Manager.commitTransaction();
            Manager = null;
            return newFamily;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddFamily);
        }
    }

    public MemberData AddHomelessMember(String nickname, String password,
                                        String familyName, String name,
                                        int gender, String phone)
            throws DataBaseError {
        try {
            FamilyData newFamily = AddFamily(familyName, "");
            MemberData newMember = AddMemberWithHome(nickname, password, newFamily.getID(), name, gender, phone);
            newFamily.setRootMemberID(newMember.getID());
            return newMember;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
        }
    }

    public MemberData AddMemberWithHome(String nickname, String password, String familyID,
                                        String name, int gender, String phone)
            throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            Manager.beginTransaction();
            MemberData newMember = Manager.createObject(MemberData.class);
            newMember.setID(String.valueOf(Manager.where(MemberData.class).findAll().size()));
            newMember.setFamilyID(familyID);
            newMember.setName(name);
            newMember.setGender(gender);
            newMember.setNickName(nickname);
            newMember.setPassword(password);
            newMember.setActivate(true);
            Manager.commitTransaction();

            try {
                AddPhone(phone, newMember.getID());
            } catch (DataBaseError e) {
                throw e;
            } catch (Exception e) {
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
            }

            Manager = null;
            return newMember;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
        }
    }

    public void AddPhone(String phone, String memberID)
            throws DataBaseSignal, DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
            else if (Result.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
            Manager.beginTransaction();
            PhoneData newPhone = Manager.createObject(PhoneData.class);
            newPhone.setMemberID(memberID);
            newPhone.setPhone(phone);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneAddedAlready);
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
        }
    }

    public void AddGameRecord(String memberID, double correctness, long date, int gameType)
            throws DataBaseSignal, DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
            else if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            Date dateNow = new Date();
            if(dateNow.getTime() < date)
                throw new DataBaseError(DataBaseError.ErrorType.AddingFutureDate);

            Manager.beginTransaction();
            GameRecordData newRecord = Manager.createObject(GameRecordData.class);
            newRecord.setRecordID(String.valueOf(Manager.where(GameRecordData.class).findAll().size()));
            newRecord.setCorrectness(correctness);
            newRecord.setGameType(gameType);
            newRecord.setMemberID(memberID);
            newRecord.setDate(date);
            newRecord.setActivate(true);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordAddedAlready);
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddGameRecord);
        }
    }

    public void AddMemberRelation(String memberA, String memberB, MemberRelationData.RELATION relation)
            throws DataBaseSignal, DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID", memberA).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
            else if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            Result = Manager.where(MemberData.class).equalTo("ID", memberB).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
            else if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);

            Manager.beginTransaction();
            MemberRelationData newRelation = Manager.createObject(MemberRelationData.class);
            newRelation.setMemberA(memberA);
            newRelation.setMemberB(memberB);
            newRelation.setRelation(relation);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationAddedAlready);
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddRelation);
        }
    }

    public void AddImage(String memberID, String name, String location, String imagePath,
                         long date, String note, String parentImage)
            throws DataBaseSignal, DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
            else if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            else if ("" != parentImage) {
                RealmResults<PictureData> Result2 = Manager.where(PictureData.class).equalTo("ID", parentImage).findAll();
                if (null == Result2)
                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
                else if (Result2.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
            }
            Date dateNow = new Date();
            if(dateNow.getTime() < date)
                throw new DataBaseError(DataBaseError.ErrorType.AddingFutureDate);

            Manager.beginTransaction();
            PictureData newImage = Manager.createObject(PictureData.class);
            newImage.setID(String.valueOf(Manager.where(PictureData.class).findAll().size()));
            if("" != imagePath)
                newImage.setImagePath(imagePath);
            else
                newImage.setImagePath(".ImageStore." + newImage.getID());
            newImage.setMemberID(memberID);
            newImage.setDate(date);
            newImage.setLocation(location);
            newImage.setName(name);
            newImage.setNote(note);
            newImage.setParentImage(parentImage);
            newImage.setActivate(true);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
        }
    }

    public void AddThirdPartyAccount(String memberID, String account, int thirdPartyType)
            throws DataBaseSignal, DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
            if (null == Result)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
            else if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            RealmResults<ThirdPartyAccountData> Result2 = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (null == Result2)
                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
            else if (Result2.size() > 0)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);

            Manager.beginTransaction();
            ThirdPartyAccountData newAccount = Manager.createObject(ThirdPartyAccountData.class);
            newAccount.setMemberID(memberID);
            newAccount.setAccount(account);
            newAccount.setThirdPartyType(thirdPartyType);
            Manager.commitTransaction();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
        }
    }

    public void AddExistMemberToExistFamily(String familyID, String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<FamilyData> ResultF = Manager.where(FamilyData.class).equalTo("ID", familyID).findAll();
            if (ResultF.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
            RealmResults<MemberData> ResultM = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
            if (ResultM.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);

            // change direct member data
            MemberData updateMember = Manager.createObject(MemberData.class, memberID);
            String oldFamilyID = updateMember.getFamilyID();
            updateMember.setFamilyID(familyID);

            // change original family data
            FamilyData updateFamily = Manager.createObject(FamilyData.class, oldFamilyID);
            updateFamily.setActivate(false);

            // check and update other original family member
            ResultM = Manager.where(MemberData.class).equalTo("familyID", oldFamilyID).findAll();
            if(ResultM.size() > 0){
                do{
                    updateMember = Manager.createObject(MemberData.class,
                            Manager.where(MemberData.class).equalTo("familyID", oldFamilyID).findAll().first().getID());
                    updateMember.setFamilyID(familyID);
                    ResultM = Manager.where(MemberData.class).equalTo("familyID", oldFamilyID).findAll();
                } while(ResultM.size() > 0);
                Manager = null;
                throw new DataBaseSignal(DataBaseSignal.SignalType.MergeTwoFamiliesSucceed);
            }
            else{
                Manager = null;
                throw new DataBaseSignal(DataBaseSignal.SignalType.AddSingleMemberToFamilySucceed);
            }
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public List<PhoneData> getPhoneList(String phone, String memberID) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<PhoneData> Results = Manager.where(PhoneData.class);
        if ("" != phone)
            Results = Results.equalTo("phone", phone);
        if ("" != memberID)
            Results = Results.equalTo("memberID", memberID);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    public List<FamilyData> getFamilyList(String ID, String name, String rootMemberID) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<FamilyData> Results = Manager.where(FamilyData.class);
        if ("" != ID)
            Results = Results.equalTo("ID", ID);
        if ("" != name)
            Results = Results.equalTo("name", name);
        if ("" != rootMemberID)
            Results = Results.equalTo("rootMemberID", rootMemberID);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<MemberData> getMemberList(String ID, String familyID, String name, String nickname) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<MemberData> Results = Manager.where(MemberData.class);
        if ("" != ID)
            Results = Results.equalTo("ID", ID);
        if ("" != familyID)
            Results = Results.equalTo("familyID", familyID);
        if ("" != name)
            Results = Results.equalTo("name", name);
        if ("" != nickname)
            Results = Results.equalTo("nickname", nickname);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<MemberRelationData> getMemberRelationList(String member) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<MemberRelationData> Results = Manager.where(MemberRelationData.class);
        if ("" != member)
            Results = Results.equalTo("memberA", member).or().equalTo("memberB", member);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    public List<GameRecordData> getGameRecordList(String recordID, String memberID, int gameType,
                                                  long dateBegin, long dateEnd) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<GameRecordData> Results = Manager.where(GameRecordData.class);
        if ("" != recordID)
            Results = Results.equalTo("recordID", recordID);
        if ("" != memberID)
            Results = Results.equalTo("memberID", memberID);
        if (0 != gameType)
            Results = Results.equalTo("gameType", gameType);
        if(0 != dateBegin)
            Results = Results.greaterThanOrEqualTo("date",dateBegin);
        if(0 != dateEnd)
            Results = Results.lessThanOrEqualTo("date",dateEnd);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<PictureData> getPictureList(String ID, String name, String memberID,
                                            String location, String parentImage) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<PictureData> Results = Manager.where(PictureData.class);
        if ("" != ID)
            Results = Results.equalTo("ID", ID);
        if ("" != name)
            Results = Results.equalTo("name", name);
        if ("" != memberID)
            Results = Results.equalTo("memberID", memberID);
        if ("" != location)
            Results = Results.equalTo("location", location);
        if ("" != parentImage)
            Results = Results.equalTo("parentImage", parentImage);
        if(0 != dateBegin)
            Results = Results.greaterThanOrEqualTo("date",dateBegin);
        if(0 != dateEnd)
            Results = Results.lessThanOrEqualTo("date",dateEnd);
        Manager = null;
        return Manager.copyFromRealm(Results.equalTo("activate",true).findAll());
    }

    public List<ThirdPartyAccountData> getThirdPartyAccountList(String memberID, String account, int thirdPartyType) {
        Manager = Realm.getDefaultInstance();
        RealmQuery<ThirdPartyAccountData> Results = Manager.where(ThirdPartyAccountData.class);
        if ("" != memberID)
            Results = Results.equalTo("memberID", memberID);
        if ("" != account)
            Results = Results.equalTo("account", account);
        if (0 != thirdPartyType)
            Results = Results.equalTo("thirdPartyType", thirdPartyType);
        Manager = null;
        return Manager.copyFromRealm(Results.findAll());
    }

    // Negative "amount" means no limitation
    public List<PictureData> getRandomPicturesFromMember(String memberID, String name, String location,
                                                         long dateBegin, long dateEnd, int amount)
    throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmQuery<PictureData> Results = Manager.where(PictureData.class);
            if ("" != name)
                Results = Results.equalTo("name", name);
            if ("" != memberID)
                Results = Results.equalTo("memberID", memberID);
            if ("" != location)
                Results = Results.equalTo("location", location);
            if (0 != dateBegin)
                Results = Results.greaterThanOrEqualTo("date", dateBegin);
            if (0 != dateEnd)
                Results = Results.lessThanOrEqualTo("date", dateEnd);

            List<PictureData> list = Manager.copyFromRealm(Results.findAll());
            if(amount >= 0) {
                if (list.size() < amount)
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            Manager = null;
            return list;
        } catch (Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    public List<PictureData> getRandomPicturesFromFamily(String familyID, String name, String location,
                                                         long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<FamilyData> Result = Manager.where(FamilyData.class).equalTo("ID",familyID).findAll();
            if(Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
            List<MemberData> memberList = Manager.copyFromRealm(Manager
                    .where(MemberData.class).equalTo("familyID",familyID).findAll());
            if(memberList.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);

            List<PictureData> list = getRandomPicturesFromMember(memberList.get(0).getID(),
                    name, location, dateBegin, dateEnd, -1);
            for(int i=1;i<memberList.size();i++)
                list.addAll(getRandomPicturesFromMember(memberList.get(i).getID(),
                        name, location, dateBegin, dateEnd, -1));

            if (amount >= 0) {
                if (list.size() < amount)
                    throw new DataBaseError(DataBaseError.ErrorType.RequiredImageNotEnough);
                Random rand = new Random();
                while (list.size() > amount) {
                    list.remove(rand.nextInt(list.size()) % list.size());
                }
            }
            Manager = null;
            return list;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public void LoginCheck_Phone(String phone, String password)
            throws DataBaseSignal, DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
        if (null == Result)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
        else if (Result.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
        else if (Result.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        else if (Match.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        else if(!Match.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        Manager = null;
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    public void LoginCheck_ThirdPartyAccount(String account, int thirdPartyType, String password)
            throws DataBaseSignal, DataBaseError {
        Manager = Realm.getDefaultInstance();
        RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                .equalTo("account", account).equalTo("thirdPartyType", thirdPartyType).findAll();
        if (null == Result)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        else if (Result.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
        else if (Result.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
        RealmResults<MemberData> Match = Manager.where(MemberData.class)
                .equalTo("ID", Result.first().getMemberID())
                .equalTo("password", password).findAll();
        if (Match.size() <= 0)
            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
        else if (Match.size() > 1)
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        else if(!Match.first().getActivate())
            throw new DataBaseError(DataBaseError.ErrorType.MemberHibernating);
        Manager = null;
        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
    }

    public void MemberHibernate(String memberID)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID", memberID).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            MemberData updateMember = Manager.createObject(MemberData.class, memberID);
            updateMember.setActivate(false);
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberHibernationSucceed);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    public void GameRecordHibernate(String recordID)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<GameRecordData> Result = Manager.where(GameRecordData.class).equalTo("recordID", recordID).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.RecordNotExist);
            GameRecordData updateRecord = Manager.createObject(GameRecordData.class, recordID);
            updateRecord.setActivate(false);
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordHibernateSucceed);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_GameRecordData);
        }
    }

    public void PictureHibernate(String imageID)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<PictureData> Result = Manager.where(PictureData.class).equalTo("ID", imageID).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.ImageNotExist);
            PictureData updateImage = Manager.createObject(PictureData.class, imageID);
            updateImage.setActivate(false);
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PictureHibernateSucceed);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }

    public void DestroyPhone(String phone)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone", phone).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.PhoneNotExist);
            PhoneData updateImage = Manager.createObject(PhoneData.class, phone);
            updateImage.deleteFromRealm();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneDeleted);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
        }
    }

    public void DestroyThirdPartyAccount(String memberID, int thirdPartyType)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
                    .equalTo("memberID", memberID).equalTo("thirdPartyType", thirdPartyType).findAll();
            if (Result.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
            else if (Result.size() > 1)
                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
            Result.first().deleteFromRealm();
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.ThirdPartyAccountDeleted);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
        }
    }

    public void UpdateFamily(String ID, String name, String rootMemberID)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<FamilyData> Results = Manager.where(FamilyData.class)
                    .equalTo("ID", ID).equalTo("activate",true).findAll();
            if (Results.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.FamilyNotExist);
            if("" != name)
                Results.first().setName(name);
            if("" != rootMemberID){
                RealmResults<MemberData> newRootMember = Manager.where(MemberData.class)
                        .equalTo("ID",rootMemberID).findAll();
                if(newRootMember.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
                else if(newRootMember.first().getFamilyID() != ID)
                    throw new DataBaseError(DataBaseError.ErrorType.SettingStrangerAsFamilyRoot);
                Results.first().setRootMemberID(rootMemberID);
            }
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.FamilyUpdated);
        } catch(Exception e){
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_FamilyData);
        }
    }

    // Negative "gender" means no changes
    public void UpdateMember(String ID, String password, int gender, String name, String nickname)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<MemberData> Results = Manager.where(MemberData.class)
                    .equalTo("ID", ID).equalTo("activate", true).findAll();
            if (Results.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
            if ("" != password)
                Results.first().setPassword(password);
            if (gender > 0)
                Results.first().setGender(gender);
            if ("" != name)
                Results.first().setName(name);
            if ("" != nickname)
                Results.first().setNickName(nickname);
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberUpdated);
        } catch(Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
        }
    }

    // When "date" is 0, no changes will be made
    public void UpdatePicture(String ID, String imagePath, String name, String memberID,
                              String location, long date, String note, String parentImage)
            throws DataBaseError, DataBaseSignal {
        try {
            Manager = Realm.getDefaultInstance();
            RealmResults<PictureData> Results = Manager.where(PictureData.class)
                    .equalTo("ID", ID).equalTo("activate", true).findAll();
            if (Results.size() <= 0)
                throw new DataBaseError(DataBaseError.ErrorType.ImageNotExist);
            if ("" != imagePath)
                Results.first().setImagePath(imagePath);
            if ("" != name)
                Results.first().setName(name);
            if ("" != memberID)
                Results.first().setMemberID(memberID);
            if ("" != location)
                Results.first().setLocation(location);
            if (0 != date)
                Results.first().setDate(date);
            if ("" != note)
                Results.first().setNote(note);
            if ("" != parentImage) {
                RealmResults<PictureData> Result2 = Manager.where(PictureData.class).equalTo("ID", parentImage).findAll();
                if (Result2.size() <= 0)
                    throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
                Results.first().setParentImage(parentImage);
            }
            Manager = null;
            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberUpdated);
        } catch(Exception e) {
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
        }
    }
}
