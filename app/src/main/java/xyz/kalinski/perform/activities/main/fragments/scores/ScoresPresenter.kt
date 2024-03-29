package xyz.kalinski.perform.activities.main.fragments.scores

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import xyz.kalinski.perform.models.ScoreHeader
import xyz.kalinski.perform.view.ViewType
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScoresPresenter @Inject constructor(val requester: ScoresRequester) : IScoresPresenter, IScoresPresenter.RequesterListener {

    var view: IScoresView? = null
    var interval: Disposable? = null

    val REFRESH_INTERVAL = 30L

    override fun initView(view: IScoresView) {
        this.view = view
    }

    override fun getScores() {
        interval = Observable.interval(REFRESH_INTERVAL, TimeUnit.SECONDS)
                .startWith(0L)
                .subscribeOn(Schedulers.newThread())
                .subscribe({ requester.getScores(this) })
    }

    override fun getList(): ArrayList<ViewType> {
        val list = arrayListOf<ViewType>()

        requester.xml?.competition?.season?.round?.groupList?.size ?: return list

        val dateSet = hashSetOf<ScoreHeader>()

        for (i in 0..requester.xml!!.competition!!.season!!.round!!.groupList!!.size - 1) {
            val group = requester.xml!!.competition!!.season!!.round!!.groupList!![i]
            if (group.matchList != null) {
                for (item in group.matchList!!) {
                    if (item.dateUtc != null) dateSet.add(ScoreHeader(item.dateUtc!!))
                    list.add(item)
                }
            }
        }

        list.addAll(0, dateSet)
        return list
    }

    override fun onDestroy() {
        view = null
        interval?.dispose()
        requester.dispose()
        requester.xml = null
    }

    override fun onItemsReceived() {
        view?.hideProgressBar()
        view?.notifyUpdate()
    }

    override fun onError() {
        view?.showError()
    }
}