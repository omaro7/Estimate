package kr.co.goms.module.common.handler;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICommonCallback  extends IInterface {
    void onSuccess(String var1) throws RemoteException;

    void onFail(String var1, int var2) throws RemoteException;

    abstract static class Stub extends Binder implements ICommonCallback {
        private static final String DESCRIPTOR = "kr.co.goms.module.common.handler.ICommonCallback";
        static final int TRANSACTION_onSuccess = 1;
        static final int TRANSACTION_onFail = 2;

        public Stub() {
            this.attachInterface(this, "kr.co.goms.module.common.handler.ICommonCallback");
        }

        public static ICommonCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("kr.co.goms.module.common.handler.ICommonCallback");
                return (ICommonCallback)(iin != null && iin instanceof ICommonCallback ? (ICommonCallback)iin : new ICommonCallback.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface("kr.co.goms.module.common.handler.ICommonCallback");
                    _arg0 = data.readString();
                    this.onSuccess(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("kr.co.goms.module.common.handler.ICommonCallback");
                    _arg0 = data.readString();
                    int _arg1 = data.readInt();
                    this.onFail(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("kr.co.goms.module.common.handler.ICommonCallback");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ICommonCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "kr.co.goms.module.common.handler.ICommonCallback";
            }

            public void onSuccess(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("kr.co.goms.module.common.handler.ICommonCallback");
                    _data.writeString(id);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onFail(String id, int errorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("kr.co.goms.module.common.handler.ICommonCallback");
                    _data.writeString(id);
                    _data.writeInt(errorCode);
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