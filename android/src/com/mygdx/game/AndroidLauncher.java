package com.mygdx.game;

import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FlappyGame(), config);
	}
}

/*public class AndroidLauncher extends FragmentActivity implements AndroidFragmentApplication.Callbacks
{
	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// 6. Finally, replace the AndroidLauncher activity content with the Libgdx Fragment.
		GameFragment fragment = new GameFragment();
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.replace(android.R.id.content, fragment);
		trans.commit();
	}

	// 4. Create a Class that extends AndroidFragmentApplication which is the Fragment implementation for Libgdx.
	private class GameFragment extends AndroidFragmentApplication
	{
		// 5. Add the initializeForView() code in the Fragment's onCreateView method.
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{  return initializeForView(new FlappyGame());   }
	}


	@Override
	public void exit() {}
}*/
