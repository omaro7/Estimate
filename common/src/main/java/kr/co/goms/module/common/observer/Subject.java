package kr.co.goms.module.common.observer;

import java.util.ArrayList;

import kr.co.goms.module.common.base.BaseBean;

public class Subject implements SubjectInterface {
    private ArrayList<ObserverInterface> observerList;
    protected BaseBean data;

    public Subject() {
    }

    public void setData(BaseBean data) {
        this.data = data;
    }

    public BaseBean getData() {
        return this.data;
    }

    public void registerObserver(ObserverInterface obj) {
        if (null != obj) {
            if (null == this.observerList) {
                this.observerList = new ArrayList();
            }

            synchronized(this) {
                int nSize = this.observerList.size();

                for(int n = 0; n < nSize; ++n) {
                    ObserverInterface curObj = (ObserverInterface)this.observerList.get(n);
                    if (curObj == obj) {
                        return;
                    }
                }

                this.observerList.add(obj);
            }
        }
    }

    public void removeObserver(ObserverInterface obj) {
        if (null != obj) {
            if (null != this.observerList) {
                synchronized(this) {
                    int nSize = this.observerList.size();

                    for(int n = 0; n < nSize; ++n) {
                        ObserverInterface curObj = (ObserverInterface)this.observerList.get(n);
                        if (curObj == obj) {
                            this.observerList.remove(n);
                            return;
                        }
                    }

                }
            }
        }
    }

    public void removeObserverAll() {
        if (null != this.observerList) {
            synchronized(this) {
                this.observerList.removeAll(this.observerList);
            }
        }
    }

    public void notifyObservers() {
        if (null != this.observerList) {
            synchronized(this) {
                int nSize = this.observerList.size();

                for(int n = nSize - 1; n >= 0; --n) {
                    try {
                        ObserverInterface curObj = (ObserverInterface)this.observerList.get(n);
                        curObj.callback(this.data);
                    } catch (Exception var7) {
                        var7.printStackTrace();
                    }
                }

            }
        }
    }
}
