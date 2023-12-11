package kr.co.goms.module.common.handler;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISampleCallback  extends IInterface {
    void onSuccess(SampleResult var1) throws RemoteException;

    void onFail(int var1, SampleResult var2) throws RemoteException;

    abstract static class Stub extends Binder implements ISampleCallback {
        private static final String DESCRIPTOR = "kr.co.goms.module.common.handler.ISampleCallback";
        static final int TRANSACTION_onSuccess = 1;
        static final int TRANSACTION_onFail = 2;

        public Stub() {
            this.attachInterface(this, "kr.co.goms.module.common.handler.ISampleCallback");
        }

        public static ISampleCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("kr.co.goms.module.common.handler.ISampleCallback");
                return (ISampleCallback)(iin != null && iin instanceof ISampleCallback ? (ISampleCallback)iin : new ISampleCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch(code) {
                case 1:
                    data.enforceInterface("kr.co.goms.module.common.handler.ISampleCallback");
                    SampleResult _arg0;
                    if (0 != data.readInt()) {
                        _arg0 = (SampleResult)SampleResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }

                    this.onSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("kr.co.goms.module.common.handler.ISampleCallback");
                    int _arg20 = data.readInt();
                    SampleResult _arg1;
                    if (0 != data.readInt()) {
                        _arg1 = (SampleResult)SampleResult.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }

                    this.onFail(_arg20, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("kr.co.goms.module.common.handler.IEnrollCardCallback");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ISampleCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "kr.co.goms.module.common.handler.ISampleCallback";
            }

            public void onSuccess(SampleResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("kr.co.goms.module.common.handler.ISampleCallback");
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFail(int errorCode, SampleResult result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("kr.co.goms.module.common.handler.ISampleCallback");
                    _data.writeInt(errorCode);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}
