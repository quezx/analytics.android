package com.quezx.analytics.common.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.quezx.analytics.MainActivity;
import com.quezx.analytics.common.constants.BranchConstant;
import com.quezx.analytics.common.constants.IntentConstant;
import com.quezx.analytics.listener.ResultCallBack;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;

public class BranchHelper {



	public void getDeepLink(Context context, BranchUniversalObject branchObject,
                            String feature, String desktopUrl, final ResultCallBack<String> urlCallback) {
		branchObject.listOnGoogleSearch(context);
		branchObject.generateShortUrl(context, getLinkProperties(feature, desktopUrl),
				new Branch.BranchLinkCreateListener() {
					@Override
					public void onLinkCreate(String url, BranchError error) {
						if (error == null) {
							urlCallback.onResultCallBack(url, null);
						} else {
							urlCallback.onResultCallBack(null, new Exception());
						}
					}
				});
	}

	private LinkProperties getLinkProperties(String feature, String desktopUrl) {
		return new LinkProperties()
				.setChannel("android")
				.setFeature(feature)
				.addControlParameter("$desktop_url", desktopUrl);
	}

	public void shareDeepLink(AppCompatActivity activity, BranchUniversalObject branchObject,
                              String feature, String desktopUrl,
                              final ResultCallBack<String> urlCallback) {
		branchObject.showShareSheet(activity,
				getLinkProperties(feature, desktopUrl),
				getShareSheetStyle(activity),
				new Branch.BranchLinkShareListener() {
					@Override
					public void onShareLinkDialogLaunched() {
					}

					@Override
					public void onShareLinkDialogDismissed() {
					}

					@Override
					public void onLinkShareResponse(String sharedLink, String sharedChannel,
                                                    BranchError error) {
						if (error == null) {
							urlCallback.onResultCallBack(sharedLink, null);
						} else {
							urlCallback.onResultCallBack(null, new Exception());
						}
					}

					@Override
					public void onChannelSelected(String channelName) {
					}
				});
	}

	private ShareSheetStyle getShareSheetStyle(Context context) {
		return new ShareSheetStyle(context, "Check this out!",
				"Candidate link: ")
				.setCopyUrlStyle(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_send),
						"Copy", "Added to clipboard")
				.setMoreOptionStyle(ContextCompat.getDrawable(context, android.R.drawable.ic_menu_search),
						"Show more")
				.addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
				.addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL)
				.addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
				.setAsFullWidthStyle(true)
				.setSharingTitle("Share With");
	}

	public boolean openActivity(Context context, String data, String activity) {
		if (activity.equalsIgnoreCase(BranchConstant.ACTIVITY_APPLICANT)) {
			Intent i = new Intent(context, MainActivity.class);
			i.putExtra(IntentConstant.ID, Integer.parseInt(data));
			i.putExtra(IntentConstant.BACK_INTENT, true);
			context.startActivity(i);
			return true;
		}
		return false;
	}
}
