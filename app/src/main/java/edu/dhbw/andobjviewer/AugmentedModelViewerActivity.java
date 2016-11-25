package edu.dhbw.andobjviewer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.graphics.LightingRenderer;
import edu.dhbw.andobjviewer.graphics.Model3D;
import edu.dhbw.andobjviewer.models.Model;
import edu.dhbw.andobjviewer.parser.ObjParser;
import edu.dhbw.andobjviewer.parser.ParseException;
import edu.dhbw.andobjviewer.parser.Util;
import edu.dhbw.andobjviewer.util.AssetsFileUtil;
import edu.dhbw.andobjviewer.util.BaseFileUtil;
import edu.dhbw.andobjviewer.util.SDCardFileUtil;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * @author Tobi
 *
 */
public class AugmentedModelViewerActivity extends AndARActivity implements SurfaceHolder.Callback {

	/**
	 * View a file in the assets folder
	 */
	public static final int TYPE_INTERNAL = 0;
	/**
	 * View a file on the sd card.
	 */
	public static final int TYPE_EXTERNAL = 1;

	public static final boolean DEBUG = false;

	/* Menu Options: */
	private final int MENU_SCALE = 0;
	private final int MENU_ROTATE = 1;
	private final int MENU_TRANSLATE = 2;
	private final int MENU_SCREENSHOT = 3;

	private int mode = MENU_SCALE;


	private Model model;
	private Model3D model3d;
	private ProgressDialog waitDialog;
	private Resources res;

	ARToolkit artoolkit;

	public AugmentedModelViewerActivity() {
		super(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setNonARRenderer(new LightingRenderer());//or might be omited
		res=getResources();
		artoolkit = getArtoolkit();
		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		getSurfaceView().getHolder().addCallback(this);
        initMenuSurface();
	}

	/**
	 * Inform the user about exceptions that occurred in background threads.
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("");
	}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	super.surfaceCreated(holder);
    	//load the model
    	//this is done here, to assure the surface was already created, so that the preview can be started
    	//after loading the model
    	if(model == null) {
			waitDialog = ProgressDialog.show(this, "",
	                getResources().getText(R.string.loading), true);
			waitDialog.show();
			new ModelLoader().execute();
		}
    }

	private void initMenuSurface() {
        ImageView translateIcon = new ImageView(this);
        translateIcon.setImageResource(R.drawable.ic_open_with_black_24dp);
        translateIcon.setPadding(20, 20, 20, 20);
        translateIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mode = MENU_TRANSLATE;
                highlightSelection(view);
            }
        });
        getMenuSurface().addView(translateIcon);

        ImageView rotateIcon = new ImageView(this);
        rotateIcon.setImageResource(R.drawable.ic_rotate_90_degrees_ccw_black_24dp);
        rotateIcon.setPadding(20, 20, 20, 20);
        rotateIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mode = MENU_ROTATE;
                highlightSelection(view);
            }
        });
        getMenuSurface().addView(rotateIcon);

        ImageView scaleIcon = new ImageView(this);
        scaleIcon.setImageResource(R.drawable.ic_zoom_out_map_black_24dp);
        scaleIcon.setColorFilter(ContextCompat.getColor(this, R.color.blue));
        scaleIcon.setPadding(20, 20, 20, 20);
        scaleIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mode = MENU_SCALE;
                highlightSelection(view);
            }
        });
        getMenuSurface().addView(scaleIcon);

        ImageView screenshotIcon = new ImageView(this);
        screenshotIcon.setImageResource(R.drawable.ic_camera_alt_black_24dp);
        screenshotIcon.setPadding(20, 20, 20, 20);
        screenshotIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                new TakeAsyncScreenshot().execute();
                ImageView imageView = (ImageView) view;
                imageView.setColorFilter(ContextCompat.getColor(AugmentedModelViewerActivity.this, R.color.blue));
            }
        });
        getMenuSurface().addView(screenshotIcon);

        ImageView myCartIcon = new ImageView(this);
        myCartIcon.setImageResource(R.drawable.ic_add_shopping_cart_black_24dp);
        myCartIcon.setPadding(20, 20, 20, 20);
        myCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(AugmentedModelViewerActivity.this, CartActivity.class);
                startActivity(intent);
                finish();
                ImageView imageView = (ImageView) view;
                imageView.setColorFilter(ContextCompat.getColor(AugmentedModelViewerActivity.this, R.color.blue));
            }
        });
        getMenuSurface().addView(myCartIcon);
    }

    private void highlightSelection(View view) {
        LinearLayout linearLayout = (LinearLayout) view.getParent();
        int childCount = linearLayout.getChildCount();
        ImageView imageView;
        for (int i = 0; i < childCount; i++) {
            imageView = (ImageView) linearLayout.getChildAt(i);
            if (view.equals(imageView)) {
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.blue));
            } else {
                imageView.clearColorFilter();
            }
        }
    }

    /**
     * Handles touch events.
     * @author Tobias Domhan
     *
     */
    class TouchEventHandler implements OnTouchListener {

    	private float lastX=0;
    	private float lastY=0;

		/* handles the touch events.
		 * the object will either be scaled, translated or rotated, dependen on the
		 * current user selected mode.
		 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
		 */
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(model!=null) {
				switch(event.getAction()) {
					//Action started
					default:
					case MotionEvent.ACTION_DOWN:
						lastX = event.getX();
						lastY = event.getY();
						break;
					//Action ongoing
					case MotionEvent.ACTION_MOVE:
						float dX = lastX - event.getX();
						float dY = lastY - event.getY();
						lastX = event.getX();
						lastY = event.getY();
						if(model != null) {
							switch(mode) {
								case MENU_SCALE:
									model.setScale(dY/100.0f);
						            break;
						        case MENU_ROTATE:
						        	model.setXrot(-1*dX);//dY-> Rotation um die X-Achse
									model.setYrot(-1*dY);//dX-> Rotation um die Y-Achse
						            break;
						        case MENU_TRANSLATE:
						        	model.setXpos(dY/10f);
									model.setYpos(dX/10f);
						        	break;
							}
						}
						break;
					//Action ended
					case MotionEvent.ACTION_CANCEL:
					case MotionEvent.ACTION_UP:
						lastX = event.getX();
						lastY = event.getY();
						break;
				}
			}
			return true;
		}

    }

	private class ModelLoader extends AsyncTask<Void, Void, Void> {


    	@Override
    	protected Void doInBackground(Void... params) {

			Intent intent = getIntent();
			Bundle data = intent.getExtras();
			int type = data.getInt("type");
			String modelFileName = data.getString("name");
			BaseFileUtil fileUtil= null;
			File modelFile=null;
			switch(type) {
			case TYPE_EXTERNAL:
				fileUtil = new SDCardFileUtil();
				modelFile =  new File(URI.create(modelFileName));
				modelFileName = modelFile.getName();
				fileUtil.setBaseFolder(modelFile.getParentFile().getAbsolutePath());
				break;
			case TYPE_INTERNAL:
				fileUtil = new AssetsFileUtil(getResources().getAssets());
				fileUtil.setBaseFolder("models/");
				break;
			}

			//read the model file:
			if(modelFileName.endsWith(".obj")) {
				ObjParser parser = new ObjParser(fileUtil);
				try {
					if(Config.DEBUG)
						Debug.startMethodTracing("AndObjViewer");
					if(type == TYPE_EXTERNAL) {
						//an external file might be trimmed
						BufferedReader modelFileReader = new BufferedReader(new FileReader(modelFile));
						String shebang = modelFileReader.readLine();
						if(!shebang.equals("#trimmed")) {
							//trim the file:
							File trimmedFile = new File(modelFile.getAbsolutePath()+".tmp");
							BufferedWriter trimmedFileWriter = new BufferedWriter(new FileWriter(trimmedFile));
							Util.trim(modelFileReader, trimmedFileWriter);
							if(modelFile.delete()) {
								trimmedFile.renameTo(modelFile);
							}
						}
					}
					if(fileUtil != null) {
						BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName);
						if(fileReader != null) {
							model = parser.parse("Model", fileReader);
							model3d = new Model3D(model);
						}
					}
					if(Config.DEBUG)
						Debug.stopMethodTracing();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
    		return null;
    	}
    	@Override
    	protected void onPostExecute(Void result) {
    		super.onPostExecute(result);
    		waitDialog.dismiss();

    		//register model
    		try {
    			if(model3d!=null)
    				artoolkit.registerARObject(model3d);
			} catch (AndARException e) {
				e.printStackTrace();
			}
			startPreview();
    	}
    }

	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {

		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/AndARScreenshot"+new Date().getTime()+".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if(errorMsg == null)
				Toast.makeText(AugmentedModelViewerActivity.this, getResources().getText(R.string.screenshotsaved), Toast.LENGTH_SHORT ).show();
			else
				Toast.makeText(AugmentedModelViewerActivity.this, getResources().getText(R.string.screenshotfailed)+errorMsg, Toast.LENGTH_SHORT ).show();
		};

	}


}
