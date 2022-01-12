package com.example.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import com.base.baselib.RxBaseActivity


class MainActivity : RxBaseActivity(), View.OnClickListener {

    //    @BindView(R.id.btn_frame)
    lateinit var btnFrame: Button

    //    @BindView(R.id.btn_tween)
    lateinit var btnTween: Button

    //    @BindView(R.id.btn_prop)
    lateinit var btnProp: Button

    //    @BindView(R.id.img)
    lateinit var img: ImageView


    override fun contentLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        super.initUI()
//        ButterKnife.bind(this)
        btnFrame = findViewById(R.id.btn_frame)
        btnTween = findViewById(R.id.btn_tween)
        btnProp = findViewById(R.id.btn_prop)
        img = findViewById(R.id.img)
        btnFrame.setOnClickListener(this)
        btnTween.setOnClickListener(this)
        btnProp.setOnClickListener(this)
    }

    var anim: AnimationDrawable? = null
    var loadAnimation: Animation? = null


    fun stopAnim() {
        anim?.stop()
        loadAnimation?.cancel()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_frame -> {
                stopAnim()
                img.setImageResource(R.drawable.anim_audio_record)
                anim = img.drawable as AnimationDrawable
                anim?.start()
            }
            R.id.btn_tween -> {
                stopAnim()
                loadAnimation = AnimationUtils.loadAnimation(this, R.anim.anim)
                loadAnimation?.apply {
                    this.duration = 2000
                    this.repeatCount = 3
                }
                img.startAnimation(loadAnimation)
            }
            R.id.btn_prop -> {
                stopAnim()


                var animator2 = ObjectAnimator.ofFloat(img, "translationX", 0f).apply {
                    this.duration = 2000

                }
                var animator = ObjectAnimator.ofFloat(img, "translationX", 200f).apply {
                    this.duration = 2000
                    this.addListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            animator2.start()
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                    })
                }
                animator2.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        animator.start()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                })

                animator.start()

            }
            R.id.img -> {

            }
        }
    }
}