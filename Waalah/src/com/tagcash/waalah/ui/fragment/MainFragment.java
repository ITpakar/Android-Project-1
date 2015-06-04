package com.tagcash.waalah.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tagcash.waalah.R;
import com.tagcash.waalah.app.Constants;
import com.tagcash.waalah.util.WAFontProvider;
import com.tagcash.waalah.view.CustomDurationViewPager;
import com.viewpagerindicator.LinePageIndicator;

@SuppressLint("InflateParams")
public class MainFragment extends Fragment implements OnClickListener {

	private CustomDurationViewPager pager;
	private MainUpcomingFragment upcoming_fragment = MainUpcomingFragment.newInstance();
	private MainMyEventsFragment myevents_fragment = MainMyEventsFragment.newInstance();
	private MainHistoryFragment history_fragment = MainHistoryFragment.newInstance();
	
	private MenuAdapter adapter;

	public MainFragment(MenuAdapter mfa) {
		super();
		
		adapter = mfa;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		this.setHasOptionsMenu(true);
		getActivity().setTitle(R.string.Favorites);
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				return (Fragment)upcoming_fragment;
			} else if (position == 1){
				return (Fragment)myevents_fragment;
			} else if (position == 2){
				return (Fragment)history_fragment;
			}

			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "Upcoming";
			} else if (position == 1){
				return "My Events";
			} else if (position == 2){
				return "History";
			}

			return "";
		}

		@Override
		public int getCount() {
			return 3;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, null);

		final ImageView img_menu = (ImageView) v.findViewById(R.id.img_menu);
		final LinearLayout layout_upcomming = (LinearLayout) v.findViewById(R.id.layout_upcoming);
		final LinearLayout layout_myevents = (LinearLayout) v.findViewById(R.id.layout_myevents);
		final LinearLayout layout_history = (LinearLayout) v.findViewById(R.id.layout_history);
		final TextView txt_upcoming = (TextView) v.findViewById(R.id.txt_upcoming);
		final TextView txt_myevents = (TextView) v.findViewById(R.id.txt_myevents);
		final TextView txt_history = (TextView) v.findViewById(R.id.txt_history);
		final ImageView img_upcoming = (ImageView) v.findViewById(R.id.img_upcoming);
		final ImageView img_myevents = (ImageView) v.findViewById(R.id.img_myevents);
		final ImageView img_history = (ImageView) v.findViewById(R.id.img_history);

		img_menu.setOnClickListener(this);
		layout_upcomming.setOnClickListener(this);
		layout_myevents.setOnClickListener(this);
		layout_history.setOnClickListener(this);
		
		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getChildFragmentManager());
		pager = (CustomDurationViewPager) v.findViewById(R.id.pager);
		pager.setScrollDurationFactor(Constants.VP_SCROLL_FACTOR);
		pager.setAdapter(adapter);

		LinePageIndicator indicator = (LinePageIndicator)v.findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				if (index == 0) {
					txt_upcoming.setTextColor(getResources().getColor(R.color.title_selected));
					txt_myevents.setTextColor(getResources().getColor(R.color.title_unselected));
					txt_history.setTextColor(getResources().getColor(R.color.title_unselected));
					img_upcoming.setVisibility(View.VISIBLE);
					img_myevents.setVisibility(View.GONE);
					img_history.setVisibility(View.GONE);
					
					if (upcoming_fragment != null)
						upcoming_fragment.manualRefresh();
					
				} else if (index == 1) {
					
					txt_upcoming.setTextColor(getResources().getColor(R.color.title_unselected));
					txt_myevents.setTextColor(getResources().getColor(R.color.title_selected));
					txt_history.setTextColor(getResources().getColor(R.color.title_unselected));
					img_upcoming.setVisibility(View.GONE);
					img_myevents.setVisibility(View.VISIBLE);
					img_history.setVisibility(View.GONE);
					
					if (myevents_fragment != null)
						myevents_fragment.manualRefresh();
					
				} else if (index == 2) {
					
					txt_upcoming.setTextColor(getResources().getColor(R.color.title_unselected));
					txt_myevents.setTextColor(getResources().getColor(R.color.title_unselected));
					txt_history.setTextColor(getResources().getColor(R.color.title_selected));
					img_upcoming.setVisibility(View.GONE);
					img_myevents.setVisibility(View.GONE);
					img_history.setVisibility(View.VISIBLE);

					if (history_fragment != null)
						history_fragment.manualRefresh();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		// set font
		txt_upcoming.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this.getActivity()));
		txt_myevents.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this.getActivity()));
		txt_history.setTypeface(WAFontProvider.getFont(WAFontProvider.HELVETICA_NEUE_LIGHT, this.getActivity()));

		pager.setCurrentItem(0);
		
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_menu:
			adapter.onMenuClicked();
			break;
		case R.id.layout_upcoming:
			pager.setCurrentItem(0);
			break;
		case R.id.layout_myevents:
			pager.setCurrentItem(1);
			break;
		case R.id.layout_history:
			pager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
}
