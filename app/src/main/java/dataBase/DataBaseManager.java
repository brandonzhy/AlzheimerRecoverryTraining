//package dataBase;
//
//import java.util.Date;
//import java.util.List;
//
//import io.realm.Realm;
//import io.realm.RealmQuery;
//import io.realm.RealmResults;
//
//public class DataBaseManager{
//    private Realm Manager;
//
//    // Only lazy deletion is safe, or ID conflict will occur
//    private FamilyData AddFamily(String name, String memberID)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            FamilyData newFamily = Manager.createObject(FamilyData.class);
//            newFamily.setID(String.valueOf(Manager.where(FamilyData.class).findAll().size()));
//            newFamily.setName(name);
//            newFamily.setRootMemberID(memberID);
//            Manager.commitTransaction();
//            Manager = null;
//            return newFamily;
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddFamily);
//        }
//        finally {
//            throw new DataBaseSignal(DataBaseSignal.SignalType.CreatingNewFamily);
//        }
//    }
//
//    public MemberData AddHomelessMember(String nickname, String password,
//                                  String familyName, String name,
//                                  Boolean gender, String phone)
//            throws DataBaseError
//    {
//        try {
//            FamilyData newFamily = AddFamily(familyName, "");
//            MemberData newMember = AddMemberWithHome(nickname,password,newFamily.getID(),name,gender,phone);
//            newFamily.setRootMemberID(newMember.getID());
//            return newMember;
//        }
//        catch (Exception e) {
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
//        }
//    }
//
//    public MemberData AddMemberWithHome(String nickname, String password,String familyID,
//                                   String name, Boolean gender, String phone)
//            throws DataBaseError
//    {
//        try {
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            MemberData newMember = Manager.createObject(MemberData.class);
//            newMember.setID(String.valueOf(Manager.where(MemberData.class).findAll().size()));
//            newMember.setFamilyID(familyID);
//            newMember.setName(name);
//            newMember.setGender(gender);
//            newMember.setNickName(nickname);
//            newMember.setPassword(password);
//
//            try {
//                AddPhone(phone, newMember.getID());
//            } catch (DataBaseError e) {
//                throw e;
//            } catch (Exception e) {
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
//            }
//
//            Manager.commitTransaction();
//            Manager = null;
//            return newMember;
//        }
//        catch (Exception e) {
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddMember);
//        }
//    }
//
//    public void AddPhone(String phone, String memberID)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone",phone).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
//            else if(Result.size() > 0)
//                throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
//            PhoneData newPhone = Manager.createObject(PhoneData.class);
//            newPhone.setMemberID(memberID);
//            newPhone.setPhone(phone);
//            Manager.commitTransaction();
//            Manager = null;
//            throw new DataBaseSignal(DataBaseSignal.SignalType.PhoneAddedAlready);
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddPhone);
//        }
//    }
//
//    public void AddGameRecord(String memberID, double correctness, Date date, int gameType)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID",memberID).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//            else if(Result.size() <= 0)
//                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//            GameRecordData newRecord = Manager.createObject(GameRecordData.class);
//            newRecord.setRecordID(String.valueOf(Manager.where(GameRecordData.class).findAll().size()));
//            newRecord.setCorrectness(correctness);
//            newRecord.setDate(date);
//            newRecord.setGameType(gameType);
//            newRecord.setMemberID(memberID);
//            Manager.commitTransaction();
//            Manager = null;
//            throw new DataBaseSignal(DataBaseSignal.SignalType.GameRecordAddedAlready);
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddGameRecord);
//        }
//    }
//
//    public void AddMemberRelation(String memberA, String memberB, MemberRelationData.RELATION relation)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID",memberA).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//            else if(Result.size() <= 0)
//                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//            Result = Manager.where(MemberData.class).equalTo("ID",memberB).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//            else if(Result.size() <= 0)
//                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//
//            MemberRelationData newRelation = Manager.createObject(MemberRelationData.class);
//            newRelation.setMemberA(memberA);
//            newRelation.setMemberB(memberB);
//            newRelation.setRelation(relation);
//            Manager.commitTransaction();
//            Manager = null;
//            throw new DataBaseSignal(DataBaseSignal.SignalType.MemberRelationAddedAlready);
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddRelation);
//        }
//    }
//
//    public void AddImage(String memberID, String name, String location, Date date, String note, String parentImage)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID",memberID).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//            else if(Result.size() <= 0)
//                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//            else if("" != parentImage){
//                RealmResults<PictureData> Result2 = Manager.where(PictureData.class).equalTo("ID",parentImage).findAll();
//                if(null == Result2)
//                    throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PictureData);
//                else if(Result2.size() <= 0)
//                    throw new DataBaseError(DataBaseError.ErrorType.ParentImageNotExist);
//            }
//
//            PictureData newImage = Manager.createObject(PictureData.class);
//            newImage.setID(String.valueOf(Manager.where(PictureData.class).findAll().size()));
//            newImage.setImagePath(".ImageStore." + newImage.getID());
//            newImage.setMemberID(memberID);
//            newImage.setDate(date);
//            newImage.setLocation(location);
//            newImage.setName(name);
//            newImage.setNote(note);
//            newImage.setParentImage(parentImage);
//            Manager.commitTransaction();
//            Manager = null;
//            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
//        }
//    }
//
//    public void AddThirdPartyAccount(String memberID, String account, int thirdPartyType)
//            throws DataBaseSignal, DataBaseError
//    {
//        try{
//            Manager = Realm.getDefaultInstance();
//            Manager.beginTransaction();
//            RealmResults<MemberData> Result = Manager.where(MemberData.class).equalTo("ID",memberID).findAll();
//            if(null == Result)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//            else if(Result.size() <= 0)
//                throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//            RealmResults<ThirdPartyAccountData> Result2 = Manager.where(ThirdPartyAccountData.class)
//                    .equalTo("account",account).equalTo("thirdPartyType",thirdPartyType).findAll();
//            if(null == Result2)
//                throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
//            else if(Result2.size() > 0)
//                throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
//
//            ThirdPartyAccountData newAccount = Manager.createObject(ThirdPartyAccountData.class);
//            newAccount.setMemberID(memberID);
//            newAccount.setAccount(account);
//            newAccount.setThirdPartyType(thirdPartyType);
//            Manager.commitTransaction();
//            Manager = null;
//            throw new DataBaseSignal(DataBaseSignal.SignalType.ImageAddedAlready);
//        }
//        catch(Exception e){
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_AddImage);
//        }
//    }
//
//    public void LoginCheck_Phone(String phone, String password)
//            throws DataBaseSignal, DataBaseError
//    {
//        Manager = Realm.getDefaultInstance();
//        RealmResults<PhoneData> Result = Manager.where(PhoneData.class).equalTo("phone",phone).findAll();
//        if(null == Result)
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_PhoneData);
//        else if(Result.size() <= 0)
//            throw new DataBaseError(DataBaseError.ErrorType.MemberNotExist);
//        else if(Result.size() > 1)
//            throw new DataBaseError(DataBaseError.ErrorType.PhoneNumberConflict);
//        RealmResults<MemberData> Match = Manager.where(MemberData.class)
//                .equalTo("ID",Result.first().getMemberID())
//                .equalTo("password",password).findAll();
//        if(Match.size() <= 0)
//            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
//        else if(Match.size() > 1)
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//        Manager = null;
//        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
//    }
//
//    public void LoginCheck_ThirdPartyAccount(String account, int thirdPartyType, String password)
//            throws DataBaseSignal, DataBaseError
//    {
//        Manager = Realm.getDefaultInstance();
//        RealmResults<ThirdPartyAccountData> Result = Manager.where(ThirdPartyAccountData.class)
//                .equalTo("account",account).equalTo("thirdPartyType",thirdPartyType).findAll();
//        if(null == Result)
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ThirdPartyAccountData);
//        else if(Result.size() <= 0)
//            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountNotExist);
//        else if(Result.size() > 1)
//            throw new DataBaseError(DataBaseError.ErrorType.ThirdPartyAccountConflict);
//        RealmResults<MemberData> Match = Manager.where(MemberData.class)
//                .equalTo("ID",Result.first().getMemberID())
//                .equalTo("password",password).findAll();
//        if(Match.size() <= 0)
//            throw new DataBaseError(DataBaseError.ErrorType.WrongLoginPassWord);
//        else if(Match.size() > 1)
//            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_MemberData);
//        Manager = null;
//        throw new DataBaseSignal(DataBaseSignal.SignalType.LoginSucceed);
//    }
//
//    public List<PhoneData> getPhoneList(String phone, String memberID){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<PhoneData> Results = Manager.where(PhoneData.class);
//        if("" != phone)
//            Results = Results.equalTo("phone",phone);
//        if("" != memberID)
//            Results = Results.equalTo("memberID",memberID);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<FamilyData> getFamilyList(String ID, String name, String rootMemberID){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<FamilyData> Results = Manager.where(FamilyData.class);
//        if("" != ID)
//            Results = Results.equalTo("ID",ID);
//        if("" != name)
//            Results = Results.equalTo("name",name);
//        if("" != rootMemberID)
//            Results = Results.equalTo("rootMemberID",rootMemberID);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<MemberData> getMemberList(String ID, String familyID, String name, String nickname){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<MemberData> Results = Manager.where(MemberData.class);
//        if("" != ID)
//            Results = Results.equalTo("ID",ID);
//        if("" != familyID)
//            Results = Results.equalTo("familyID",familyID);
//        if("" != name)
//            Results = Results.equalTo("name",name);
//        if("" != nickname)
//            Results = Results.equalTo("nickname",nickname);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<MemberRelationData> getMemberRelationList(String member){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<MemberRelationData> Results = Manager.where(MemberRelationData.class);
//        if("" != member)
//            Results = Results.equalTo("memberA",member).or().equalTo("memberB",member);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<GameRecordData> getGameRecordList(String recordID, String memberID, int gameType){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<GameRecordData> Results = Manager.where(GameRecordData.class);
//        if("" != recordID)
//            Results = Results.equalTo("recordID",recordID);
//        if("" != memberID)
//            Results = Results.equalTo("memberID",memberID);
//        if(0 != gameType)
//            Results = Results.equalTo("gameType",gameType);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<PictureData> getPictureList(String ID, String name, String memberID, String location, String parentImage){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<PictureData> Results = Manager.where(PictureData.class);
//        if("" != ID)
//            Results = Results.equalTo("ID",ID);
//        if("" != name)
//            Results = Results.equalTo("name",name);
//        if("" != memberID)
//            Results = Results.equalTo("memberID",memberID);
//        if("" != location)
//            Results = Results.equalTo("location",location);
//        if("" != parentImage)
//            Results = Results.equalTo("parentImage",parentImage);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//
//    public List<ThirdPartyAccountData> getThirdPartyAccountList(String memberID, String account, int thirdPartyType){
//        Manager = Realm.getDefaultInstance();
//        RealmQuery<ThirdPartyAccountData> Results = Manager.where(ThirdPartyAccountData.class);
//        if("" != memberID)
//            Results = Results.equalTo("memberID",memberID);
//        if("" != account)
//            Results = Results.equalTo("account",account);
//        if(0 != thirdPartyType)
//            Results = Results.equalTo("thirdPartyType",thirdPartyType);
//        return Manager.copyFromRealm(Results.findAll());
//    }
//}
