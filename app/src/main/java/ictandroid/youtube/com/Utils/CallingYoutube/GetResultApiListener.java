package ictandroid.youtube.com.Utils.CallingYoutube;
import com.google.api.services.youtube.model.Channel;

public interface GetResultApiListener {
    void onGetInfoChannel(Channel infoChannel);
    void onCheckSub(boolean existed);
    void onCheckSubFail();
}
