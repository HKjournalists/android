package com.kplus.car.activity;

import com.kplus.car.R;
import com.kplus.car.model.Refund;
import com.kplus.car.model.json.AddRefundInfoJson;
import com.kplus.car.model.response.AddRefundInfoReponse;
import com.kplus.car.model.response.request.AddRefundInfoRequest;
import com.kplus.car.util.StringUtils;
import com.kplus.car.util.ToastUtil;

import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RefundActivity extends BaseActivity implements OnClickListener
{
	private Button orderButton,btBankCard,btAlipay,btCancelCard;
	private TextView receivables,tvTitle,userBankName,userAlipayName,userAlipayNumber;
	private EditText userCardName,tvCardNumber;
	private View rlSelectCard,bankMessage,ailMessage;
	private ImageView ivLeft;
	private View leftView;
	public int type;
	public String account,name,bank;
	@Override
	protected void initView()
	{
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.daze_order_refund);	
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.daze_title_layout);
		leftView = findViewById(R.id.leftView);
		ivLeft = (ImageView) findViewById(R.id.ivLeft);
		ivLeft.setImageResource(R.drawable.daze_btn_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		orderButton = (Button) findViewById(R.id.orderButton);
		receivables = (TextView) findViewById(R.id.receivables);
		btBankCard =(Button) findViewById(R.id.btBankCard);
		btAlipay = (Button)findViewById(R.id.btAlipay);
		rlSelectCard = findViewById(R.id.rlSelectCard);
		userCardName = (EditText) findViewById(R.id.userCardName);
		tvCardNumber = (EditText) findViewById(R.id.tvCardNumber);
		btCancelCard = (Button) findViewById(R.id.btCancelCard);
		userBankName = (TextView) findViewById(R.id.userBankName);
		userAlipayName = (TextView) findViewById(R.id.userAlipayName);
		userAlipayNumber = (TextView) findViewById(R.id.userAlipayNumber);
		bankMessage = findViewById(R.id.bankMessage);
		ailMessage = findViewById(R.id.ailMessage);
		tvTitle.setText("填写收款账号");		
	}
	

	@Override
	protected void loadData() {
		Refund refund = (Refund) getIntent().getSerializableExtra("refund");
		try{
		if(refund != null){
			if(refund.getType() != null && refund.getType() == 2){
				receivables.setText("银行卡");
				type = 2;
				bankMessage.setVisibility(View.VISIBLE);			
				ailMessage.setVisibility(View.GONE);
				userCardName.setText(refund.getName());
				tvCardNumber.setText(refund.getAccount());
				userBankName.setText(refund.getBank());
			}
			else{
				receivables.setText("支付宝");
				type = 1;
				bankMessage.setVisibility(View.GONE);			
				ailMessage.setVisibility(View.VISIBLE);
				userAlipayName.setText(refund.getName());
				userAlipayNumber.setText(refund.getAccount());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void setListener() {
		leftView.setOnClickListener(this);
		receivables.setOnClickListener(this);
		orderButton.setOnClickListener(this);
		btBankCard.setOnClickListener(this);
		btAlipay.setOnClickListener(this);
		btCancelCard.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(receivables)){
			rlSelectCard.setVisibility(View.VISIBLE);
		}else if(v.equals(orderButton)){
			if(receivables.getText().equals("银行卡")){
				account = tvCardNumber.getText().toString();
				name = userCardName.getText().toString();
				bank = userBankName.getText().toString();
				type = 2;
			}else if(receivables.getText().equals("支付宝")){
				name = userAlipayName.getText().toString();
				account = userAlipayNumber.getText().toString();
				bank = null;
				type = 1;
			}
			fillFinish();
		}else if(v.equals(btBankCard)){
			rlSelectCard.setVisibility(View.GONE);	
			receivables.setText("银行卡");
			bankMessage.setVisibility(View.VISIBLE);			
			ailMessage.setVisibility(View.GONE);
		}else if(v.equals(btAlipay)){
			rlSelectCard.setVisibility(View.GONE);
			receivables.setText("支付宝");
			bankMessage.setVisibility(View.GONE);
			ailMessage.setVisibility(View.VISIBLE);
		}else if(v.equals(btCancelCard)){
			rlSelectCard.setVisibility(View.GONE);
		}
		else if(v.equals(leftView)){
			finish();
		}
	}
	
	public void fillFinish(){
		if(StringUtils.isEmpty(account)){
			ToastUtil.TextToast(RefundActivity.this, "请输入账号号码", Toast.LENGTH_SHORT, Gravity.CENTER);	
			return;
		}
		if(StringUtils.isEmpty(name)){
			ToastUtil.TextToast(RefundActivity.this, "请输入账户名", Toast.LENGTH_SHORT, Gravity.CENTER);
			return;			
		}
		if(type == 2){
			if(StringUtils.isEmpty(bank)){
				ToastUtil.TextToast(RefundActivity.this, "请输入开户行", Toast.LENGTH_SHORT, Gravity.CENTER);
				return;
			}
		}
		new AddRefundInfoTask().execute();
	}	
	
	class AddRefundInfoTask extends AsyncTask<Void, Void, AddRefundInfoReponse>{
		@Override
		protected AddRefundInfoReponse doInBackground(Void... params) {//异步线程
			try{
				AddRefundInfoRequest request = new AddRefundInfoRequest();
				request.setParams(mApplication.getId(),type, account, name, bank);
				return mApplication.client.execute(request);
			}
			catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
		@Override                    
		protected void onPostExecute(AddRefundInfoReponse result) {
			try{
				if(result != null && result.getCode() != null && result.getCode() == 0){//判定
					AddRefundInfoJson data = result.getData();
					if(data != null){
						if(data.getResult() != null && data.getResult())
							finish();
						else
							ToastUtil.TextToast(RefundActivity.this, StringUtils.isEmpty(result.getMsg()) ? "保存失败，请稍后重试" : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
					else{
						ToastUtil.TextToast(RefundActivity.this, StringUtils.isEmpty(result.getMsg()) ? "保存失败，请稍后重试" : result.getMsg(), Toast.LENGTH_SHORT, Gravity.CENTER);
					}
				}
				else{
					String msg = null;
					msg = result == null ? null : result.getMsg();
					ToastUtil.TextToast(RefundActivity.this, StringUtils.isEmpty(msg) ? "保存失败，请稍后重试" : msg, Toast.LENGTH_SHORT, Gravity.CENTER);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
}
