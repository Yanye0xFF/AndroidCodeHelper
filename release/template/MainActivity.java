import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
	
	private Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		initViews();
		initParams();
    }

	private void initViews() {
	
	}

	private void initParams() {
		 mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

	}
	
	private void showToast(final String str) {
		mToast.setText(str);
		mToast.show();
	}

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }

	@Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}