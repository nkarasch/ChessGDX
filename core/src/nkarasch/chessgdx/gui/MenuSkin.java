package nkarasch.chessgdx.gui;

import nkarasch.chessgdx.util.AssetHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MenuSkin extends Skin {

	public final static String TEX_LOW_ALPHA_GREY = "low_alpha";

	public final static String IB_KNIGHT_PROMOTION = "knight_promotion";
	public final static String IB_BISHOP_PROMOTION = "bishop_promotion";
	public final static String IB_ROOK_PROMOTION = "rook_promotion";
	public final static String IB_QUEEN_PROMOTION = "queen_promotion";
	public final static String IMG_LOADING_ICON = "loading_icon";
	public final static String IB_SETTINGS = "settings_button";
	public final static String IB_SLIDER = "slider_button";
	public final static String IB_EXIT = "exit_button";
	public final static String IB_NEW = "new_button";
	public final static String LB_LARGE = "large_label";
	public final static String LB_LARGE_NOBG = "large_label_nobg";
	public final static String LB_LARGE_WHITE = "large_white_label";
	public final static String LB_SMALL_WHITE = "small_white_label";
	public final static String LS_COLOR_CHOOSER = "color_chooser_list";

	private final Pixmap mHighAlphaGrey;
	private final Pixmap mLowAlphaGrey;
	private final Pixmap mFullWhite;
	private final Texture mLowAlphaGreyTexture;
	private final Texture mHighAlphaGreyTexture;
	private final Texture mFullWhiteTexture;

	public MenuSkin() {

		AssetHandler ah = AssetHandler.getInstance();

		mHighAlphaGrey = new Pixmap(4, 4, Format.RGBA8888);
		mHighAlphaGrey.setColor(0.0f, 0.0f, 0.0f, 0.8f);
		mHighAlphaGrey.fill();

		mLowAlphaGrey = new Pixmap(4, 4, Format.RGBA8888);
		mLowAlphaGrey.setColor(0.0f, 0.0f, 0.0f, 0.5f);
		mLowAlphaGrey.fill();

		mFullWhite = new Pixmap(4, 4, Format.RGBA8888);
		mFullWhite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		mFullWhite.fill();

		mLowAlphaGreyTexture = new Texture(mLowAlphaGrey);
		mHighAlphaGreyTexture = new Texture(mHighAlphaGrey);

		TextureRegionDrawable lowAlpha = new TextureRegionDrawable(new TextureRegion(mLowAlphaGreyTexture));
		add(TEX_LOW_ALPHA_GREY, lowAlpha);

		TextureRegionDrawable highAlpha = new TextureRegionDrawable(new TextureRegion(mHighAlphaGreyTexture));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UI/Tuffy.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 20;

		BitmapFont smallFont = generator.generateFont(parameter);
		parameter.size = 36;

		BitmapFont largeFont = generator.generateFont(parameter);
		generator.dispose();
		add("default", largeFont);

		ImageButtonStyle settingsButtonStyle = new ImageButtonStyle();
		settingsButtonStyle.imageUp = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/settings-unclicked.jpg")));
		settingsButtonStyle.imageDown = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/settings-clicked.jpg")));
		add(IB_SETTINGS, settingsButtonStyle);

		ImageButtonStyle sliderStyle = new ImageButtonStyle();
		sliderStyle.imageUp = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/slide-unclicked.jpg")));
		sliderStyle.imageDown = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/slide-clicked.jpg")));
		add(IB_SLIDER, sliderStyle);

		ImageButtonStyle exitStyle = new ImageButtonStyle();
		exitStyle.imageUp = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/exit.jpg")));
		add(IB_EXIT, exitStyle);

		TextButtonStyle newGameStyle = new TextButtonStyle(); 
		newGameStyle.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/button-down.jpg")));
		newGameStyle.down = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/button-down.jpg")));
		newGameStyle.font = largeFont;
		newGameStyle.fontColor = Color.WHITE;
		add(IB_NEW, newGameStyle);

		ImageButtonStyle knightButton = new ImageButtonStyle();
		knightButton.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/knight-silhouette.png")));
		add(IB_KNIGHT_PROMOTION, knightButton);

		ImageButtonStyle bishopButton = new ImageButtonStyle();
		bishopButton.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/bishop-silhouette.png")));
		add(IB_BISHOP_PROMOTION, bishopButton);

		ImageButtonStyle rookButton = new ImageButtonStyle();
		rookButton.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/rook-silhouette.png")));
		add(IB_ROOK_PROMOTION, rookButton);

		ImageButtonStyle queenButton = new ImageButtonStyle();
		queenButton.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/queen-silhouette.png")));
		add(IB_QUEEN_PROMOTION, queenButton);

		add(IMG_LOADING_ICON, ah.getTexture("UI/loading.png"));

		LabelStyle largeLabel = new LabelStyle();
		largeLabel.background = lowAlpha;
		largeLabel.font = largeFont;
		largeLabel.fontColor = Color.WHITE;
		add(LB_LARGE, largeLabel);

		LabelStyle largeLabelNoBackground = new LabelStyle();
		largeLabelNoBackground.font = largeFont;
		largeLabelNoBackground.fontColor = Color.WHITE;
		add(LB_LARGE_NOBG, largeLabelNoBackground);

		LabelStyle smallLabel = new LabelStyle();
		smallLabel.font = smallFont;
		smallLabel.fontColor = Color.WHITE;
		add("default", smallLabel);

		LabelStyle largeWhiteLabel = new LabelStyle();
		largeWhiteLabel.font = largeFont;
		largeWhiteLabel.fontColor = Color.WHITE;
		largeWhiteLabel.background = highAlpha;
		add(LB_LARGE_WHITE, largeWhiteLabel);

		LabelStyle smallWhiteLabel = new LabelStyle();
		smallWhiteLabel.font = smallFont;
		smallWhiteLabel.fontColor = Color.WHITE;
		add(LB_SMALL_WHITE, smallWhiteLabel);

		ScrollPaneStyle scrollPane = new ScrollPaneStyle();
		scrollPane.background = lowAlpha;
		add("default", scrollPane);

		ListStyle listStyle = new ListStyle();
		listStyle.background = lowAlpha;
		listStyle.font = smallFont;
		listStyle.selection = highAlpha;
		listStyle.fontColorSelected = Color.WHITE;
		listStyle.fontColorUnselected = Color.WHITE;
		add("default", listStyle);

		mFullWhiteTexture = new Texture(mFullWhite);
		ListStyle colorListStyle = new ListStyle();
		colorListStyle.background = lowAlpha;
		colorListStyle.font = smallFont;
		colorListStyle.selection = new TextureRegionDrawable(new TextureRegion(mFullWhiteTexture));
		colorListStyle.fontColorSelected = Color.BLACK;
		colorListStyle.fontColorUnselected = Color.WHITE;
		add(LS_COLOR_CHOOSER, colorListStyle);

		SelectBoxStyle selectBoxStyle = new SelectBoxStyle();
		selectBoxStyle.font = largeFont;
		selectBoxStyle.fontColor = Color.WHITE;
		selectBoxStyle.scrollStyle = scrollPane;
		selectBoxStyle.listStyle = listStyle;
		add("default", selectBoxStyle);

		CheckBoxStyle checkboxStyle = new CheckBoxStyle();
		checkboxStyle.font = smallFont;
		checkboxStyle.checkboxOff = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/checkbox-unchecked.jpg")));
		checkboxStyle.checkboxOn = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/checkbox-checked.jpg")));
		add("default", checkboxStyle);

		TextButtonStyle ibStyle = new TextButtonStyle();
		ibStyle.up = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/button-up.jpg")));
		ibStyle.down = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/button-down.jpg")));
		ibStyle.font = smallFont;
		ibStyle.fontColor = Color.WHITE;
		add("default", ibStyle);

		WindowStyle window = new WindowStyle();
		window.titleFont = largeFont;
		window.stageBackground = new TextureRegionDrawable(new TextureRegion(mLowAlphaGreyTexture));
		add("default", window);

		TextFieldStyle textField = new TextFieldStyle();
		textField.font = smallFont;
		textField.fontColor = Color.WHITE;
		textField.background = lowAlpha;
		textField.disabledBackground = highAlpha;
		textField.cursor = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/text-cursor.png")));
		add("default", textField);

		SliderStyle slider = new SliderStyle();
		slider.background = highAlpha;
		slider.knob = new TextureRegionDrawable(new TextureRegion(ah.getTexture("UI/slider-knob.png")));
		add("default-horizontal", slider);
	}

	/**
	 * Destroys the Pixmap and Textures created with them, all non-generated
	 * textures that are used are destroyed by AssetHandler
	 */
	public void dispose() {
		mHighAlphaGrey.dispose();
		mLowAlphaGrey.dispose();
		mFullWhite.dispose();
		mLowAlphaGreyTexture.dispose();
		mHighAlphaGreyTexture.dispose();
		mFullWhiteTexture.dispose();

		super.dispose();
	}
}
