package com.project.sysumobilelibrary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.project.sysumobilelibrary.adapter.MyLikeListViewAdapter;
import com.project.sysumobilelibrary.adapter.MySearchListViewAdapter;
import com.project.sysumobilelibrary.entity.LikeBook;
import com.project.sysumobilelibrary.global.MyApplication;
import com.project.sysumobilelibrary.utils.MyDBHelper;

import dmax.dialog.SpotsDialog;
import android.R.integer;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class LikeFragment extends Fragment {
	private static final String TAG = "LikeFragment";
	private View view;
	private PullToRefreshListView myListView;
	private ImageView ivBack;
	private TextView tvTitle;
	private TextView tvFlush;
	private boolean canShow = false;
	public static ArrayList<LikeBook> likeBooks = new ArrayList<LikeBook>();
	public MyLikeListViewAdapter adapter;
	private static AlertDialog loading;

	private static Integer next_no = 0;
	private final static Integer page_num = 10;

	private final static int CODE_TOAST_MSG = 0;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_TOAST_MSG:
				Toast.makeText(getActivity(), msg.obj.toString(),
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	public void myToast(String msg) {
		handler.obtainMessage(CODE_TOAST_MSG, msg).sendToTarget();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_like, container, false);
		initView();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (likeBooks.size() == 0) {
			next_no = 0;
			loading.show();
			addLikeBooks();
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// http://www.th7.cn/Program/Android/201411/321041.shtml
		super.onHiddenChanged(hidden);
		if (!hidden) {
			if (likeBooks.size() == 0) {
				next_no = 0;
				loading.show();
				addLikeBooks();
			}
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			// Log.e(TAG, likeBooks.size()+"");
		}
	}

	public static void addLikeBookToHead(LikeBook book) {
		likeBooks.add(0, book);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		likeBooks.get(0).setLike_time(new Date().getTime());
		// if (adapter != null){
		// adapter.notifyDataSetChanged();
		// }
	}

	public static void delLikeBook(String doc_number) {
		for (int i = 0; i < likeBooks.size(); ++i) {
			LikeBook book = likeBooks.get(i);
			if (book.getDoc_number().equals(doc_number)) {
				likeBooks.remove(i);
				// if (adapter != null){
				// adapter.notifyDataSetChanged();
				// }
				break;
			}
		}
	}

	private void addLikeBooks() {
		MyDBHelper myDBHelper = new MyDBHelper(getActivity());
		ArrayList<LikeBook> books = myDBHelper.queryLikeBooks(MyApplication
				.getUser().getUsername(), next_no, page_num);
		int tmp = next_no;
		if (books.size() == 0) {
			if (canShow){
				myToast("û�и�����ղ���Ŷ����ȥ�ղؼ�����...");
				canShow = false;
			}
			if (myListView.isRefreshing()) {
				new FinishRefresh().execute();
			}
			if (loading.isShowing()) {
				loading.dismiss();
			}
			return;
		}
		for (LikeBook likeBook : books) {
			likeBooks.add(likeBook);
			next_no++;
		}

		if (myListView.isRefreshing()) {
			new FinishRefresh().execute();
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		myListView.getRefreshableView().setSelection(tmp);
		if (loading.isShowing()) {
			loading.dismiss();
		}
		canShow = false;
	}

	private void initView() {
		loading = new SpotsDialog(getActivity());
		ivBack = (ImageView) view.findViewById(R.id.iv_back);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvFlush = (TextView) view.findViewById(R.id.tv_flush);
		// ivBack.setVisibility(View.GONE);
		tvTitle.setText("�ҵ��ղ�");
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RadioButton rButton = (RadioButton) getActivity().findViewById(
						R.id.rb_search);
				rButton.performClick();
			}
		});
		tvFlush.setVisibility(View.GONE);
		tvFlush.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				next_no = 0;
				likeBooks.clear();
				loading.show();
				myListView.getRefreshableView().smoothScrollToPosition(0);
				canShow = true;
				addLikeBooks();
				// http://www.oschina.net/code/snippet_46820_45980 ģ���û�����
			}
		});

		initListView();
	}

	private void initListView() {
		myListView = (PullToRefreshListView) view
				.findViewById(R.id.pull_refresh_list);
		myListView.setEmptyView(view.findViewById(R.id.empty));

		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// ���ƽ���͸���Ķ���Ч��
		animation.setDuration(500); // ����ʱ�������
		set.addAnimation(animation); // ���붯������
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);
		myListView.setLayoutAnimation(controller); // ListView ���ö���Ч��

		myListView.setMode(Mode.BOTH);
		myListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Log.e(TAG, "����ˢ��");
				likeBooks.clear();
				next_no = 0;
				canShow = true;
				addLikeBooks();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				Log.e(TAG, "��������");
				canShow = true;
				addLikeBooks();
			}

		});
		adapter = new MyLikeListViewAdapter(getActivity(), likeBooks);
		myListView.setAdapter(adapter);
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			canShow = false;
			myListView.onRefreshComplete();
		}
	}
}