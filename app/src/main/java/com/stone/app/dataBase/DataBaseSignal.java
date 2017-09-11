package com.stone.app.dataBase;

public class DataBaseSignal extends Exception {
    private final SignalType type;

    public enum SignalType {
        PhoneAddedAlready,
        GameRecordAddedAlready,
        MemberRelationAddedAlready,
        ImageAddedAlready,
        ThirdPartyAccountAddedAlready,

        MemberHibernationSucceed,
        GameRecordHibernateSucceed,
        PictureHibernateSucceed,
        PhoneDeleted,
        MemberRelationDeleted,
        OneThirdPartyAccountDeleted,
        AllThirdPartyAccountDeleted,

        FamilyUpdated,
        MemberUpdated,
        PictureUpdated,

        LoginSucceed,
        AddSingleMemberToFamilySucceed,
        MergeTwoFamiliesSucceed,
        AutoTransplantationSucceed,
        UnknownSignal_DataBaseManager
    }

    DataBaseSignal(SignalType type) {
        this.type = type;
    }

    public SignalType getSignalType() {
        return type;
    }
}
