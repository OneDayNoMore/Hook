package cn.sanlicun.pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.sanlicun.pay.util.PreUtils;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etAsyn, etSync, etSign, etAccount;
    private Button btnSubmit, btnBack;
    private PreUtils preUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initDate();
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.ed_account);
        etAsyn = (EditText) findViewById(R.id.ed_asyn);
        etSync = (EditText) findViewById(R.id.et_sync);
        etSign = (EditText) findViewById(R.id.ed_sign);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
    }

    private void initDate() {
        preUtils=new PreUtils();
        String account = preUtils.getString(PreUtils.PRE_ACCOUNT, "");
        String sign = preUtils.getString(PreUtils.PRE_SIGN, "");
        String asyn = preUtils.getString(PreUtils.PRE_ASYN, "");
        String sync = preUtils.getString(PreUtils.PRE_SYNC, "");
        etAccount.setText(account);
        etSign.setText(sign);
        etSync.setText(sync);
        etAsyn.setText(asyn);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                Submit();

                break;
            case R.id.btn_back:
                SettingActivity.this.finish();
                break;
        }

    }

    private void Submit() {
        String account = etAccount.getText().toString().trim();
        String sign = etSign.getText().toString().trim();
        String asyn = etAsyn.getText().toString().trim();
        String sync = etSync.getText().toString().trim();

        preUtils.setString(PreUtils.PRE_ACCOUNT, account);
        preUtils.setString(PreUtils.PRE_SIGN, sign);
        preUtils.setString(PreUtils.PRE_ASYN, asyn);
        preUtils.setString(PreUtils.PRE_SYNC, sync);
        Toast.makeText(SettingActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
    }
}
