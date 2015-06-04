package com.tagcash.waalah.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.tagcash.waalah.view.WAProgressDialog;

public class BaseFragment extends Fragment {

	public LinearLayout layout_content;
	public LinearLayout layout_empty;

	public WAProgressDialog dlg_progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		this.setHasOptionsMenu(true);

		dlg_progress = new WAProgressDialog(getActivity());
	}

	public void showEmptyLayout(boolean show) {
		if (layout_content == null || layout_empty == null)
			return;
		
		if (show) {
			layout_content.setVisibility(View.GONE);
			layout_empty.setVisibility(View.VISIBLE);
		} else {
			layout_content.setVisibility(View.VISIBLE);
			layout_empty.setVisibility(View.GONE);
		}
	}

	public interface BaseFragmentInterface {
		void manualRefresh();
	}
}
