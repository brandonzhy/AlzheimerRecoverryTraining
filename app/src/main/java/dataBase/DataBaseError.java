//package dataBase;
//
//public class DataBaseError extends Exception {
//    private final ErrorType type;
//
//    enum ErrorType {
//        PhoneNumberConflict,
//        RelationConflict,
//        ThirdPartyAccountConflict,
//
//        MemberNotExist,
//        ParentImageNotExist,
//        ThirdPartyAccountNotExist,
//
//        WrongLoginPassWord,
//
//        UnknownError_AddFamily,
//        UnknownError_AddMember,
//        UnknownError_AddGameRecord,
//        UnknownError_AddPhone,
//        UnknownError_AddRelation,
//        UnknownError_AddImage,
//
//        UnknownError_PhoneData,
//        UnknownError_MemberData,
//        UnknownError_PictureData,
//        UnknownError_ThirdPartyAccountData
//    }
//
//    public DataBaseError(ErrorType type){
//        this.type = type;
//    }
//
//    public ErrorType getErrorType(){
//        return type;
//    }
//}
