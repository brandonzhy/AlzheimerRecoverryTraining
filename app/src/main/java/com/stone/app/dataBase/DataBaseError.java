package com.stone.app.dataBase;

public class DataBaseError extends Exception {
    private final ErrorType type;
    public enum ErrorType {
        RealmResultAutoUpdateFail,
        RealmListExceptionallyUpdated,

        PhoneNumberConflict,
        ThirdPartyAccountConflict,

        ImageNotExist,
        PhoneNotExist,
        FamilyNotExist,
        MemberNotExist,
        RecordNotExist,
        ParentImageNotExist,
        MemberRelationNotExist,
        ThirdPartyAccountNotExist,

        FamilyHibernating,
        MemberHibernating,
        RecordHibernating,
        PictureHibernating,

        NotStandardID,
        NotStandardPhone,
        NotStandardFactor,
        IllegalName_DigitExistInRealName,
        IllegalName_DisapprovedCharacter,
        IllegalName_ChineseMingleWithEnglish,

        NoteTooLong,
        AddingFutureDate,
        WrongLoginPassWord,
        MemberHasFamilyAlready,
        RequiredImageNotEnough,
        SettingStrangerAsFamilyRoot,

        RelationError_TryingToCombineTwoLinkedTree,
        RelationError_Bigamy,
        RelationError_Redundant,
        RelationError_HomoMarriage,
        RelationError_AlloTransplanting,
        RelationError_UnknownTransplanting,
        RelationError_Blood_TwoChildStructure,
        RelationError_Blood_TwoParentStructure,
        RelationError_Blood_ThreeSiblingStructure,
        RelationError_Blood_MixedThreeBloodStructure,
        RelationError_Blood_SeparateParentFromSiblingStructure,

        NotStandardType,
        UnspecifiedGender,
        UnspecifiedRelation,

        UnknownError_AddImage,
        UnknownError_AddPhone,
        UnknownError_AddFamily,
        UnknownError_AddMember,
        UnknownError_AddRelation,
        UnknownError_AddGameRecord,
        UnknownError_AddThirdPartyAccount,

        UnknownError_AutoTransplanting,
        UnknownError_MakeParentUnique,
        UnknownError_FindSiblingListHead,
        UnknownError_FindSiblingListTail,

        UnknownError_PhoneData,
        UnknownError_MemberData,
        UnknownError_FamilyData,
        UnknownError_PictureData,
        UnknownError_GameRecordData,
        UnknownError_MemberRelationData,
        UnknownError_ThirdPartyAccountData
    }

    DataBaseError(ErrorType type){
        this.type = type;
    }

    public ErrorType getErrorType(){
        return type;
    }
}