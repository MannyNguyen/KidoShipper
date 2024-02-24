package vn.kido.ship.Api;

import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

import rx.Subscriber;
import vn.kido.ship.Class.GlobalClass;
import vn.kido.ship.OauthActivity;

public abstract class ISubscriber extends Subscriber {
    @Override
    public void onCompleted() {
    }


    @Override
    public void onError(Throwable e) {
        Toast.makeText(GlobalClass.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        done();
    }

    @Override
    public void onNext(Object response) {
        try {
            done();
            JSONObject jsonObject;
            if (!(response instanceof JSONObject)) {
                jsonObject = new JSONObject(response.toString());
            } else {
                jsonObject = (JSONObject) response;
            }

            int code = jsonObject.getInt("code");
            if (code != 1) {
                if(code==-1){
                    Intent i= new Intent(GlobalClass.getActivity(),OauthActivity.class);
                    GlobalClass.getActivity().startActivity(i);
                    GlobalClass.getActivity().finish();
                    return;

                }
                //error
                JSONObject errorMessage = jsonObject.getJSONObject("error_message");
                Toast.makeText(GlobalClass.getActivity(), errorMessage.getString("vn") + "", Toast.LENGTH_LONG).show();
                return;
            }
            excute(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Gọi khi xong tiến trình voi code = 1
    public abstract void excute(JSONObject jsonObject);

    //khi chay xong tien trinh (tat progress)
    public void done() {

    }

}
