package kr.co.goms.module.common.observer;

public interface SubjectInterface {
    void registerObserver(ObserverInterface var1);
    void removeObserver(ObserverInterface var1);
    void removeObserverAll();
    void notifyObservers();
}
