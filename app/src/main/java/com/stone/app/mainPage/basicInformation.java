//package com.stone.app.mainPage;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.stone.app.R;
//
//
//public class basicInformation extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_information);
//
////        TextView tv_name = (TextView) findViewById(R.id.tv_name);
////        Button btn_myinfo_back = (Button) findViewById(R.id.btn_myinfo_back);
////        Button btn_modify = (Button) findViewById(R.id.btn_modify);
////        Intent intent = getIntent();
////        String memberName = intent.getStringExtra("memberName");
////        String memberID = intent.getStringExtra("memberID");
////        String memberNickName = intent.getStringExtra("memberNickName");
////        int memberGender = intent.getIntExtra("memberGender",0);
////
////        tv_name.setText(memberName);
////
////        btn_myinfo_back.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                finish();
////            }
////        });
////        btn_modify.setOnClickListener(new View.OnClickListener() {
////            @Override
////
////            public void onClick(View v) {
////
////                Intent intent = new Intent(basicInformation.this, ChangeMessage.class);
////                startActivityForResult(intent, 1);
////            }
////
////
////        });
////
////    }
////
////        @Override
////
////        public void onActivityResult(int requestCode,int resultCode,Intent data){
////            switch(requestCode){
////                case 1:
////                    if(resultCode==RESULT_OK){
////
////                        String returnedData=data.getStringExtra("memberName");
////                        TextView name2 = (TextView)findViewById(R.id.textView10);
////                        name2.setText(returnedData);
////                    }
////                    break;
////                default:
////            }
////
//        }
//
//}
