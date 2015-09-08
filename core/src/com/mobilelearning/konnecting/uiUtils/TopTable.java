package com.mobilelearning.konnecting.uiUtils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mobilelearning.konnecting.Assets;
import com.mobilelearning.konnecting.SavedData;

/**
 * Created by AFFonseca on 04/08/2015.
 */
public class TopTable extends Table {
    private ImageButton backButton;
    private Label scoreLabel, commentLabel;

    public TopTable(String title, String comment, boolean showScore, final BackButtonCallback callback) {
        super();

        top().padTop(10f).padRight(22f).defaults().padBottom(10f);

        Image topRegion = new Image(Assets.miscellaneous.findRegion("top_panel"));
        setSize(topRegion.getPrefWidth(), topRegion.getPrefHeight());
        addActor(topRegion);


        Label.LabelStyle titleStyle = new Label.LabelStyle(Assets.uiSkin.getFont("default-font"), Color.WHITE);
        Label titleLabel = new Label(title, titleStyle);
        titleLabel.setFontScale(0.55f);
        add(titleLabel).left().width(351f);

        Label.LabelStyle scoreStyle = new Label.LabelStyle(Assets.uiSkin.getFont("default-font"), Color.BLACK);
        scoreLabel = new Label("0pt", scoreStyle);
        scoreLabel.setFontScale(0.55f);
        add(scoreLabel).row(); scoreLabel.setVisible(showScore);

        Label.LabelStyle commentStyle = new Label.LabelStyle(Assets.uiSkin.getFont("arial"), Color.BLACK);
        commentLabel = new Label(comment, commentStyle); commentLabel.setAlignment(Align.center);
        commentLabel.setFontScale(0.5f); commentLabel.setWrap(true);
        add(commentLabel).width(485f).colspan(2);

        backButton = new ImageButton(
                new TextureRegionDrawable(Assets.miscellaneous.findRegion("backarrow_up")),
                new TextureRegionDrawable(Assets.miscellaneous.findRegion("backarrow_down"))
        );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if(callback != null)
                    callback.onClicked();
            }
        });
        backButton.setPosition(556f, (getHeight()-backButton.getPrefHeight())/2+10f);
        addActor(backButton);
    }

    public ImageButton getBackButton(){
        return backButton;
    }

    public Label getCommentLabel(){
        return commentLabel;
    }

    public Label getScoreLabel(){
        return scoreLabel;
    }

    public interface BackButtonCallback{
        void onClicked();
    }
}
