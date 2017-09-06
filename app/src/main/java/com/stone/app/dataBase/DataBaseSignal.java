package com.stone.app.dataBase;

public class DataBaseSignal extends Exception {
    private final SignalType type;

    enum SignalType{
        PhoneAddedAlready,
        GameRecordAddedAlready,
        MemberRelationAddedAlready,
        ImageAddedAlready,

        MemberHibernationSucceed,
        GameRecordHibernateSucceed,
        PictureHibernateSucceed,
        PhoneDeleted,
        ThirdPartyAccountDeleted,

        FamilyUpdated,
        MemberUpdated,
        RelationUpdated,
        PictureUpdated,

        LoginSucceed,
        AddSingleMemberToFamilySucceed,
        MergeTwoFamiliesSucceed,
        UnknownSignal
    }

    public DataBaseSignal(SignalType type){
        this.type = type;
    }

    public SignalType getSignalType(){
        return type;
    }
}
