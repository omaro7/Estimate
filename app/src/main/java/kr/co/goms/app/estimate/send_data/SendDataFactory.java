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
        COM_LIST("114"),
        COM_INSERT("115"),
        COM_UPDATE("116"),
        COM_DELETE("117"),

        EST_LIST("120"),
        EST_INSERT("121"),
        EST_UPDATE("122"),
        EST_DELETE("123"),
        EST_TEMP_ITEM_INSERT("124"),
        EST_TEMP_ITEM_DELETE("125"),
        EST_ITEM_LIST("126"),  //견적서 수정 시, 상품리스트 가져오기
        EST_ITEM_DELETE("127"),  //견적서 수정 시, 상품리스트 삭제하기
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
        }else if(dataType.ordinal() == DATA_TYPE.CLI_UPDATE.ordinal()) {
            sendData = new CliUpdateData();
        }else if(dataType.ordinal() == DATA_TYPE.CLI_DELETE.ordinal()) {
            sendData = new CliDeleteData();
        }else if(dataType.ordinal() == DATA_TYPE.COM_LIST.ordinal()) {
            sendData = new ComListData();
        }else if(dataType.ordinal() == DATA_TYPE.COM_INSERT.ordinal()) {
            sendData = new ComInsertData();
        }else if(dataType.ordinal() == DATA_TYPE.COM_UPDATE.ordinal()) {
            sendData = new ComUpdateData();
        }else if(dataType.ordinal() == DATA_TYPE.COM_DELETE.ordinal()) {
            sendData = new ComDeleteData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_LIST.ordinal()) {
            sendData = new EstListData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_INSERT.ordinal()) {
            sendData = new EstInsertData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_UPDATE.ordinal()) {
            sendData = new EstUpdateData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_DELETE.ordinal()) {
            sendData = new EstDeleteData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_TEMP_ITEM_INSERT.ordinal()) {
            sendData = new EstTempItemInsertData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_TEMP_ITEM_DELETE.ordinal()) {
            sendData = new EstTempItemDeleteData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_ITEM_LIST.ordinal()){
            sendData = new EstItemListData();
        }else if(dataType.ordinal() == DATA_TYPE.EST_ITEM_DELETE.ordinal()){
            sendData = new EstItemDeleteData();
        }

        return sendData;
    }

}
