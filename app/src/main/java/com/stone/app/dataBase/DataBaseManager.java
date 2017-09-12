package com.stone.app.dataBase;

import android.util.Log;

import java.util.List;

import io.realm.Realm;

public class DataBaseManager {
    private Realm Manager;
    private static boolean activateDB = false;
    private static DataMediation dm = new DataMediation();

    // Used to draw the local focused family member relation tree
    public static final class RelationRadiationChart {
        public String spouse;
        public List<String> parentList;
        public List<String> siblingList;
        public List<String> childList;
        public List<String> oneselfList;
        public List<String> predecessorList;
    }

    // In case that there exist an uncommitted transaction
    private synchronized void ProtectiveCommitTransaction() throws Exception {
        Manager.cancelTransaction();
    }

    // Check if database manager is available now
    public boolean ifDataBaseAwake() {
        return activateDB;
    }

    public synchronized void ActivateDataBase() throws DataBaseError {
        try {
            if (activateDB) {
                Log.i("TAG", "ActivateDataBase : RealmDataBaseHyperpyrexia");
                throw new DataBaseError(DataBaseError.ErrorType.RealmDataBaseHyperpyrexia);
            }
            try {
                Manager = Realm.getDefaultInstance();
            } catch (Exception e) {
                Log.i("TAG", "ActivateDataBase : " + e.getMessage() + e.getLocalizedMessage());
                throw new DataBaseError(DataBaseError.ErrorType.RealmDataBaseParalytic);
            }
            activateDB = true;
        } catch (DataBaseError e) {
            throw e;
        } catch (Exception e) {
            Log.i("TAG", "ActivateDataBase : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_ActivateDataBaseManager);
        }
    }

    public synchronized void CloseDataBase() throws DataBaseError {
        try {
            ProtectiveCommitTransaction();
        } catch (Exception e) {
            Log.i("TAG", "CloseDataBase : " + e.getMessage());
        }
        if (!activateDB) {
            Log.i("TAG", "CloseDataBase : RealmDataBaseHibernate");
            throw new DataBaseError(DataBaseError.ErrorType.RealmDataBaseHibernate);
        }
        try {
            Manager = null;
            activateDB = false;
        } catch (Exception e) {
            Log.i("TAG", "CloseDataBase : " + e.getMessage() + e.getLocalizedMessage());
            throw new DataBaseError(DataBaseError.ErrorType.UnknownError_HibernateDataBaseManager);
        }
    }

    // Count the distance between two people, return -1 means unlinked
    // Users can identify weight for each type of relation
    public synchronized int Stadiometry(String memberA, String memberB,
                                        int indexParent, int indexSpouse, int indexSibling,
                                        int indexDivorce, int indexUnknown, int indexFriend)
            throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.Stadiometry(Manager, memberA, memberB,
                    indexParent, indexSpouse, indexSibling, indexDivorce, indexUnknown, indexFriend);
        } finally {
            CloseDataBase();
        }
    }

    // Oneself relation weighs 0, all the other relations weigh 1
    public synchronized int Stadiometry(String memberA, String memberB) throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.Stadiometry(Manager, memberA, memberB, 1, 1, 1, 1, 1, 1);
        } finally {
            CloseDataBase();
        }
    }

    // Translate relation between two members into words
    // Return "" to represent that they are unlinked
    public synchronized String Transcription(String memberA, String memberB) throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.Transcription(Manager, memberA, memberB);
        } finally {
            CloseDataBase();
        }
    }

    // Attention : Remember to import .DataBaseManager.RelationRadiationChart && .DataBaseManager.RelationStrengthIndex
    // Draw the local focused family member relation tree
    public synchronized RelationRadiationChart GetLocalFocusedMap(String member) throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.GetLocalFocusedMap(Manager, member);
        } finally {
            CloseDataBase();
        }
    }

    // Only lazy deletion is safe, or ID conflict will occur
    public synchronized FamilyData AddFamily(String name, String memberID, String portraitID)
            throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.AddFamily(Manager, name, memberID, portraitID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized MemberData AddMember(String nickname, String password, String familyID,
                                             String name, int gender, String phone, String portraitID)
            throws DataBaseError {
        ActivateDataBase();
        try {
            return dm.AddMember(Manager, nickname, password, familyID, name, gender, phone, portraitID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void AddExistMemberToExistFamily(String familyID, String memberID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.AddExistMemberToExistFamily(Manager, familyID, memberID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void AddPhone(String phone, String memberID)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.AddPhone(Manager, phone, memberID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void AddGameRecord(String memberID, double factor, long date, int gameType)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.AddGameRecord(Manager, memberID, factor, date, gameType);
        } finally {
            CloseDataBase();
        }
    }

    // Attention : A is parent to B / A is hasband and B is wife
    public synchronized void AddMemberRelation(String memberA, String memberB, int relation)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.AddMemberRelation(Manager, memberA, memberB, relation);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void AddImage(String memberID, String name, String location, String imagePath,
                                      long date, String note, String parentImage)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.AddImage(Manager, memberID, name, location, imagePath, date, note, parentImage);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void AddThirdPartyAccount(String memberID, String account, int thirdPartyType)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.AddThirdPartyAccount(Manager, memberID, account, thirdPartyType);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized String getFamilyPortraitPath(String familyID) throws DataBaseError {
        ActivateDataBase();
        try {
            String l = dm.getFamilyPortraitPath(Manager, familyID);
            if (null == l) {
                Log.i("TAG", "getFamilyPortraitPath : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized String getMemberPortraitPath(String memberID) throws DataBaseError {
        ActivateDataBase();
        try {
            String l = dm.getMemberPortraitPath(Manager, memberID);
            if (null == l) {
                Log.i("TAG", "getMemberPortraitPath : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized List<PhoneData> getPhoneList(String phone, String memberID) throws DataBaseError {
        ActivateDataBase();
        try {
            List<PhoneData> l = dm.getPhoneList(Manager, phone, memberID);
            if (null == l) {
                Log.i("TAG", "getPhoneList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized List<FamilyData> getFamilyList(String ID, String name, String rootMemberID) throws DataBaseError {
        ActivateDataBase();
        try {
            List<FamilyData> l = dm.getFamilyList(Manager, ID, name, rootMemberID);
            if (null == l) {
                Log.i("TAG", "getFamilyList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Note : "nickname" could match "name" as well
    public synchronized List<MemberData> getMemberList(String ID, String familyID, String name, String nickname) throws DataBaseError {
        ActivateDataBase();
        try {
            List<MemberData> l = dm.getMemberList(Manager, ID, familyID, name, nickname);
            if (null == l) {
                Log.i("TAG", "getMemberList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Note : Member could be inactive; relation "0" means no restriction
    public synchronized List<MemberRelationData> getMemberRelationList(String member, int relation) throws DataBaseError {
        ActivateDataBase();
        try {
            List<MemberRelationData> l = dm.getMemberRelationList(Manager, member, relation);
            if (null == l) {
                Log.i("TAG", "getMemberRelationList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized List<GameRecordData> getGameRecordList(String recordID, String memberID, int gameType,
                                                               long dateBegin, long dateEnd) throws DataBaseError {
        ActivateDataBase();
        try {
            List<GameRecordData> l = dm.getGameRecordList(Manager, recordID, memberID, gameType, dateBegin, dateEnd);
            if (null == l) {
                Log.i("TAG", "getGameRecordList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized List<PictureData> getPictureList(String ID, String name, String memberID,
                                                         String location, String parentImage,
                                                         long dateBegin, long dateEnd) throws DataBaseError {
        ActivateDataBase();
        try {
            List<PictureData> l = dm.getPictureList(Manager, ID, name, memberID, location, parentImage, dateBegin, dateEnd);
            if (null == l) {
                Log.i("TAG", "getPictureList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized List<ThirdPartyAccountData> getThirdPartyAccountList(String memberID, String account, int thirdPartyType) throws DataBaseError {
        ActivateDataBase();
        try {
            List<ThirdPartyAccountData> l = dm.getThirdPartyAccountList(Manager, memberID, account, thirdPartyType);
            if (null == l) {
                Log.i("TAG", "getThirdPartyAccountList : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Negative "amount" means no limitation
    // "FromMember" means all the images captured are from the given member (memberID)
    public synchronized List<PictureData> getRandomPicturesFromMember(String memberID, String name, String location,
                                                                      long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        ActivateDataBase();
        try {
            List<PictureData> l = dm.getRandomPicturesFromMember(Manager, memberID, name, location, dateBegin, dateEnd, amount);
            if (null == l) {
                Log.i("TAG", "getRandomPicturesFromMember : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Negative "amount" means no limitation
    // You can restrict distance from root member and define the index of different relations
    // "amount" means picture number, "range" means distance from focused member (FocusedMemberID)
    public synchronized List<PictureData> getRandomPicturesFromFamily(String familyID, String name, String location,
                                                                      long dateBegin, long dateEnd, int amount,
                                                                      String FocusedMemberID, int range,
                                                                      int indexParent, int indexSpouse, int indexSibling,
                                                                      int indexDivorce, int indexUnknown, int indexFriend)
            throws DataBaseError {
        ActivateDataBase();
        try {
            List<PictureData> l = dm.getRandomPicturesFromFamily(Manager,
                    familyID, name, location, dateBegin, dateEnd, amount,
                    FocusedMemberID, range, indexParent, indexSpouse,
                    indexSibling, indexDivorce, indexUnknown, indexFriend);
            if (null == l) {
                Log.i("TAG", "getRandomPicturesFromFamily : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Negative "amount" means no limitation
    // You can restrict distance from root member and standard relation weight is used
    // "amount" means picture number, "range" means distance from focused member (FocusedMemberID)
    public synchronized List<PictureData> getRandomPicturesFromFamily(String familyID, String name, String location,
                                                                      long dateBegin, long dateEnd, int amount,
                                                                      String FocusedMemberID, int range)
            throws DataBaseError {
        ActivateDataBase();
        try {
            List<PictureData> l = dm.getRandomPicturesFromFamily(Manager,
                    familyID, name, location, dateBegin, dateEnd, amount,
                    FocusedMemberID, range, 1, 1, 1, 1, 1, 1);
            if (null == l) {
                Log.i("TAG", "getRandomPicturesFromFamily : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    // Negative "amount" means no limitation
    // There is no restriction on distance
    public synchronized List<PictureData> getRandomPicturesFromFamily(String familyID, String name, String location,
                                                                      long dateBegin, long dateEnd, int amount)
            throws DataBaseError {
        ActivateDataBase();
        try {
            List<PictureData> l = dm.getRandomPicturesFromFamily(Manager,
                    familyID, name, location, dateBegin, dateEnd, amount, "", 0, 0, 0, 0, 0, 0, 0);
            if (null == l) {
                Log.i("TAG", "getRandomPicturesFromFamily : RequiredResultsReturnNULL");
                throw new DataBaseError(DataBaseError.ErrorType.RequiredResultsReturnNULL);
            } else
                return l;
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void LoginCheck_Phone(String phone, String password)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.LoginCheck_Phone(Manager, phone, password);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void LoginCheck_ThirdPartyAccount(String account, int thirdPartyType, String password)
            throws DataBaseSignal, DataBaseError {
        ActivateDataBase();
        try {
            dm.LoginCheck_ThirdPartyAccount(Manager, account, thirdPartyType, password);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void MemberHibernate(String memberID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.MemberHibernate(Manager, memberID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void GameRecordHibernate(String recordID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.GameRecordHibernate(Manager, recordID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void PictureHibernate(String imageID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.PictureHibernate(Manager, imageID);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void DestroyPhone(String phone)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.DestroyPhone(Manager, phone);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void DestroyThirdPartyAccount(String memberID, int thirdPartyType, String account)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.DestroyThirdPartyAccount(Manager, memberID, thirdPartyType, account);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void DestroyThirdPartyAccount(String memberID, int thirdPartyType)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.DestroyThirdPartyAccount(Manager, memberID, thirdPartyType);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void DestroyRelation(String memberA, String memberB)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.DestroyRelation(Manager, memberA, memberB);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void Divorce(String memberA, String memberB)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.Divorce(Manager, memberA, memberB);
        } finally {
            CloseDataBase();
        }
    }

    public synchronized void UpdateFamily(String ID, String name,
                                          String rootMemberID, String portraitID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.UpdateFamily(Manager, ID, name, rootMemberID, portraitID);
        } finally {
            CloseDataBase();
        }
    }

    // Negative "gender" means no changes
    public synchronized void UpdateMember(String ID, String password, int gender,
                                          String name, String nickname, String portraitID)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.UpdateMember(Manager, ID, password, gender, name, nickname, portraitID);
        } finally {
            CloseDataBase();
        }
    }

    // When "date" is 0, no changes will be made
    public synchronized void UpdatePicture(String ID, String imagePath, String name, String memberID,
                                           String location, long date, String note, String parentImage)
            throws DataBaseError, DataBaseSignal {
        ActivateDataBase();
        try {
            dm.UpdatePicture(Manager, ID, imagePath, name, memberID, location, date, note, parentImage);
        } finally {
            CloseDataBase();
        }
    }
}
