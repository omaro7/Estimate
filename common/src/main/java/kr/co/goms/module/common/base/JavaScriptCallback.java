package kr.co.goms.module.common.base;

public abstract class JavaScriptCallback implements WaterCallBack {
    public JavaScriptCallback() {
    }

    public abstract void callback(BaseBean var1);

    protected String getFunction(String javascript) {
        String result = null;
        int indexFirst = javascript.indexOf(":") + 1;
        int indexLast = javascript.indexOf("(");
        result = javascript.substring(indexFirst, indexLast);
        return result;
    }

    protected String getData(String javascript) {
        String result = null;
        int indexFirst = javascript.indexOf("'") + 1;
        int indexLast = javascript.lastIndexOf("'");
        result = javascript.substring(indexFirst, indexLast);
        return result;
    }

    protected String[] getDataList(String javascript) {
        String[] result = null;
        int indexFirst = javascript.indexOf("'") + 1;
        int indexLast = javascript.lastIndexOf("'");
        String tempData = javascript.substring(indexFirst, indexLast);
        result = tempData.split("','");
        return result;
    }
}
