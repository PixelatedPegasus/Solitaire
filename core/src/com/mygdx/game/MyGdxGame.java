package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.*;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Timer;

import java.util.*;

public class MyGdxGame implements ApplicationListener{


	class CardStack extends Group {
		private Stack<Card> stack;

		public CardStack() {
			stack = new Stack<Card>();
		}

		public void addCard(Card card) {
			stack.add(card);
		}

		public Card popCard() {
			if(!stack.isEmpty()) {
				Card card = stack.get(0);
				stack.remove(0);
				return card;
			}
			return null;
		}

		// Implement the full form of draw() so we can handle rotation and scaling.
		public void draw(Batch batch, float alpha) {

			for(Card card : stack) {
				card.draw(batch, alpha);
			}
		}
	}

    // A simple card
    class Card extends Actor {
        private TextureRegion _texture;

        public Card(TextureRegion texture) {
            _texture = texture;
            setBounds(getX(), getY(), _texture.getRegionWidth(), _texture.getRegionHeight());

			final Actor actor = this;

            this.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttons) {
                	MoveByAction moveAction = new MoveByAction();
                	moveAction.setAmount(x - actor.getWidth() / 2, y - actor.getHeight() /2);
                	moveAction.setDuration(.25f);
                	actor.addAction(moveAction);
                    System.out.println("Touched " + getName() + " at x: " + x + " y: " + y);
                    return true;
                }
            });


            this.addListener(new DragListener() {
                public void drag (InputEvent event, float x, float y, int pointer) {
                    actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() /2);
                }
            });

            this.addListener(new ClickListener() {
				@Override
				public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
					super.enter(event, x, y, pointer, fromActor);
					if( pointer == -1 )
						actor.scaleBy(.25f);
				}

				@Override
				public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
					super.exit(event, x, y, pointer, toActor);
					if( pointer == -1 )
						actor.scaleBy(-.25f);
				}
			});

        }

        // Implement the full form of draw() so we can handle rotation and scaling.
        public void draw(Batch batch, float alpha) {
            batch.draw(_texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getRotation());
        }
    }

    private CardStack cardStack;
    private Card[] cards;
	private Stage stage;



	@Override
	public void create() {
		stage = new Stage();

		final TextureRegion cardTexture = new TextureRegion(new Texture("data/card.png"));

		cardStack = new CardStack();
		Card card = new Card(cardTexture);
		card.setPosition(200, 200);

		card.setName("Test Card");

		stage.addActor(card);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
