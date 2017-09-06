package com.stone.app.dataBase;

public class DataBaseError extends Exception {
    private final ErrorType type;

    enum ErrorType {
        PhoneNumberConflict,
        RelationConflict,
        ThirdPartyAccountConflict,

        ImageNotExist,
        PhoneNotExist,
        FamilyNotExist,
        MemberNotExist,
        RecordNotExist,
        ParentImageNotExist,
        ThirdPartyAccountNotExist,

        WrongLoginPassWord,
        MemberHibernating,
        AddingFutureDate,
        RequiredImageNotEnough,
        SettingStrangerAsFamilyRoot,

        UnknownError_AddFamily,
        UnknownError_AddMember,
        UnknownError_AddGameRecord,
        UnknownError_AddPhone,
        UnknownError_AddRelation,
        UnknownError_AddImage,

        UnknownError_PhoneData,
        UnknownError_MemberData,
        UnknownError_FamilyData,
        UnknownError_PictureData,
        UnknownError_GameRecordData,
        UnknownError_ThirdPartyAccountData
    }

    public DataBaseError(ErrorType type){
        this.type = type;
    }

    public ErrorType getErrorType(){
        return type;
    }
}
