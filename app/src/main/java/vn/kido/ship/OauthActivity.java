package vn.kido.ship;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;

import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.Fragment.Oauth.LoginFragment;
import vn.kido.ship.Helper.FragmentHelper;


public class OauthActivity extends FragmentActivity {
    LinearLayout ln;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        GlobalClass.setActivity(OauthActivity.this);
        FragmentHelper.addfull(LoginFragment.newInstance());
        ln = (LinearLayout) findViewById(R.id.support);
        ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "+" + getString(R.string.phone_support)));
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Fragment fragment = FragmentHelper.getActiveFragment(OauthActivity.this);
        if (fragment instanceof LoginFragment) {
            finishAffinity();
            return;
        }

        FragmentHelper.pop(OauthActivity.this);
    }
}
