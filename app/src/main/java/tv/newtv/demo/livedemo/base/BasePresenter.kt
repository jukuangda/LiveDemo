package tv.newtv.demo.livedemo.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter protected constructor() : IPresenter {
    private val mCompositeDisposable = CompositeDisposable()

    override fun subscribe() {}

    override fun unsubscribe() {
        mCompositeDisposable.clear()
    }

    protected fun addDisposable(disposable: Disposable?) {
        mCompositeDisposable.add(disposable!!)
    }
}