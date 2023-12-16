package kr.co.goms.app.estimate.send_data;


public class SendDataFactory {

    public enum DATA_TYPE{
        ITEM_LIST("100"),
        ITEM_INSERT("101"),
        ITEM_UPDATE("102"),
        ITEM_DELETE("103"),
        CLI_LIST("110"),
        CLI_INSERT("111"),
        CLI_UPDATE("112"),
        CLI_DELETE("113"),
        ;

        String mRequestUrlCode;
        DATA_TYPE(String request_url_code) {
            this.mRequestUrlCode = request_url_code;
        }

        public String getRequestUrlCode() {
            return mRequestUrlCode;
        }

    }

    public Object createSendData(DATA_TYPE dataType){
        Object sendData = null;

        if(dataType.ordinal() == DATA_TYPE.ITEM_LIST.ordinal()) {
            sendData = new ItemListData();
        }else if(dataType.ordinal() == DATA_TYPE.ITEM_INSERT.ordinal()) {
            sendData = new ItemInsertData();
        }else if(dataType.ordinal() == DATA_TYPE.ITEM_DELETE.ordinal()) {
            sendData = new ItemDeleteData();
        }else if(dataType.ordinal() == DATA_TYPE.CLI_LIST.ordinal()) {
            sendData = new CliListData();
        }else if(dataType.ordinal() == DATA_TYPE.CLI_INSERT.ordinal()) {
            sendData = new CliInsertData();
        }else if(dataType.ordinal() == DATA_TYPE.CLI_DELETE.ordinal()) {
            sendData = new CliDeleteData();
        }
        return sendData;
    }

}
