package com.stone.app.dataBase;

public class DataBaseError extends Exception {
    private final ErrorType type;

    public enum ErrorType {
        RealmDataBaseHibernate,
        RealmDataBaseParalytic,
        RealmDataBaseHyperpyrexia,
        RealmResultAutoUpdateFail,
        RealmListExceptionallyUpdated,

        PhoneNumberConflict,
        ThirdPartyAccountConflict,

        ImageNotExist,
        PhoneNotExist,
        FamilyNotExist,
        MemberNotExist,
        RecordNotExist,
        PortraitNotExist,
        ImagePathNotExist,
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
        NotStandardDateLength,
        IllegalName_DigitExistInRealName,
        IllegalName_DisapprovedCharacter,
        IllegalName_ChineseMingleWithEnglish,

        NoteTooLong,
        AddingFutureDate,
        WrongLoginPassWord,
        MemberHasFamilyAlready,
        RequiredImageNotEnough,
        RequiredResultsReturnNULL,
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

        UnknownError_Stadiometry,
        UnknownError_Transcription,
        UnknownError_MarrowPuncture,
        UnknownError_GetLocalFocusedMap,
        UnknownError_AutoTransplanting,
        UnknownError_FindSiblingListHead,
        UnknownError_FindSiblingListTail,
        UnknownError_ActivateDataBaseManager,
        UnknownError_HibernateDataBaseManager,

        UnknownError_PhoneData,
        UnknownError_MemberData,
        UnknownError_FamilyData,
        UnknownError_PictureData,
        UnknownError_GameRecordData,
        UnknownError_MemberRelationData,
        UnknownError_ThirdPartyAccountData,
        UnknownError_DataBaseManager
    }

    DataBaseError(ErrorType type) {
        this.type = type;
    }

    public ErrorType getErrorType() {
        return type;
    }
}
