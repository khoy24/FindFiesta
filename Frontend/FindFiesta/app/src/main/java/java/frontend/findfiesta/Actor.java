package java.frontend.findfiesta;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class Actor extends AppCompatActivity {

    //    @GET
    /**
     * the current user
     */
    private static Actor user;
    /**
     * current username
     */
    private static String userName;
    /**
     * username of the account to be interacted with
     */
    private static String secondUserName;
    /**
     * current user's email
     */
    private static String email;
    /**
     * email of the account to be interacted with
     */
    private static String secondEmail;
    /**
     * password of the account to be interacted with
     */
    private static String secondPassword;
    /**
     * current users password
     */
    private static String password;

    /**
     * Id of the current user
     */
    private static String currentId;
    /**
     * Id of the account to be interacted with
     */
    private static String secondId;
    /**
     * current theme of the user on the instance (login)
     */
    private static int currentTheme;

    /**
     * instance of a google map
     */
    private static GoogleMap mMap;

    /**
     * current profile picture bitmap
     */
    private static Bitmap currentProfilePic;

    /**
     * boolean representing if the user is enabled or not
     */
    private static boolean userEnabled;

    /**
     * user type represented by ids
     */
    private static int userType;

    /**
     * list of users who messaged
     */
    private static ArrayList<String> messagesArrList = new ArrayList<>();

    /**
     * list of chatroom id numbers to correspond to users messaging
     */
    private static ArrayList<Integer> chatArrList = new ArrayList<>();

    /**
     * chat message for current DM
     */
    private static String chatMessage;

    /**
     * keeps track of the current report
     */
    private static int reportId;
    /**
     * keeps track of the second users type
     */
    private static int secondUserType;


    /**
     * blank Actor constructor
     */
    private Actor() {
        // private constructor to prevent instantiation
    }

    /**
     * retrieves the current instance of Actor
     * @return user
     */
    public static synchronized Actor getInstance() {
        if (user == null) {
            user = new Actor();
        }
        return user;
    }

    /**
     * retrieves the username of the current user
     * @return
     */
    public static String getCurrentUsername() {
        return userName;
    }

    /**
     * sets username of the current user
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * gets email of the current user
     * @return
     */
    public static String getCurrentEmail() {
        return email;
    }

    /**
     * sets email of the current user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * gets password of the current user
     * @return password
     */
    public static String getCurrentPassword() {
        return password;
    }

    /**
     * sets password of the current user
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * retrieves id of the current user instance
     * @return currentId
     */
    public static String getCurrentId() {
        return currentId;
    }

    /**
     * sets the Id of the instance user
     * @param id
     */
    public void setId(String id){
        currentId = id;
    }

    /**
     * sets username of the account being interacted with
     * @param username
     */
    public void setSecondUsername(String username){
        secondUserName = username;
    }

    /**
     * gets username of the account being interacted with
     * @return secondUserName
     */
    public static String getSecondUsername() {
        return secondUserName;
    }

    /**
     * gets password of the account being interacted with
     * @return
     */
    public static String getSecondPassword() {
        return secondPassword;
    }

    /**
     * gets the email of the account being interacted with
     * @return secondEmail
     */
    public static String getSecondEmail(){
        return secondEmail;
    }

    /**
     * sets the email of the account being interacted with
     * @param email
     */
    public void setSecondEmail(String email){
        secondEmail = email;
    }

    /**
     * Sets the password of the account being interacted with
     * @param password
     */
    public void setSecondPassword(String password){
        secondPassword = password;
    }

    /**
     * sets the secondary account ID (account that the primary interacts with)
     * @param id
     */
    public void setSecondId(String id){
        secondId = id;
    }

    /**
     * gets the secondary account ID (account that the primary interacts with)
     * @return secondId
     */
    public static String getSecondId(){
        return secondId;
    }

//    current themes for UI theme feature

    /**
     * sets the current theme of the user
     * @param theme
     */
    public void setCurrentTheme(int theme) {
        if (theme == 0 || theme == 2131820633){
            currentTheme = R.style.Base_Theme_FindFiesta;
        } else if (theme == 1 || theme == 2131820634) {
            currentTheme = R.style.Base_Theme_Green;
        } else if (theme == 2 || theme == 2131820666) {
            currentTheme = R.style.Base_Theme_Pink;
        }
//        setTheme(currentTheme);
    }

    /**
     * Retrieves current theme
     * @return currentTheme
     */
    public static int getCurrentTheme() {
        return currentTheme;
    }

    /**
     * sets current map
     * @param map
     */
    public void setCurrentMap(GoogleMap map){
        mMap = map;
    }

    /**
     * returns current google map
     * @return mMap
     */
    public static GoogleMap getCurrentMap(){
        return mMap;
    }

    /**
     * sets current users profile pic
     */
    public void setCurrentProfilePic(Bitmap image){
        currentProfilePic = image;
    }

    /**
     * retrieves currentProfilePic
     */
    public static Bitmap getCurrentProfilePic(){
        return currentProfilePic;
    }

    /**
     * changes the user to be enabled/disabled
     */
    public void setUserEnabled(Boolean enabled){
        userEnabled = enabled;
    }

    /**
     * gets whether the user enabled their account or not
     */
    public static Boolean getEnabled(){
        return userEnabled;
    }


    /**
     * sets the list of messages
     */
    public void setMessagesArrList(ArrayList<String> list){

        messagesArrList = list;
    }

    /**
     * gets the list of messages
     */
    public static ArrayList<String> getMessagesArrList(){
        return messagesArrList;
    }


    /**
     * sets the chat arrary list 
     */
    public void setChatArrList(ArrayList<Integer> ints) {
        chatArrList = ints;
    }

    /**
     * gets chat integers as an array list
     */
    public static ArrayList<Integer> getChatArrList(){
        return chatArrList;
    }

    /**
     * sets the chatMessage for the current dm
     */
    public void setChatMessage(String message){
        chatMessage = message;
    }

    /**
     * get chat message as string
     */
    public static String getChatMessage(){
        return chatMessage;
    }

    /**
     * sets usertype (user, guest, mod, admin) = (0,1,2,3) (4 = in application to be a mod)
     *
     * @param userType
     */
    public void setUserType(int userType){
        this.userType = userType;
    }

    /**
     * sets the second users user type
     */
    public void setSecondUserType(int secondUserType){
        this.secondUserType = secondUserType;
    }

    /**
     * gets the second users user type
     */
    public static int getSecondUserType(){
        return secondUserType;
    }

    /**
     * retrieves the current accounts usertype
     * @return
     */
    public static int getUserType(){
        return userType;
    }

    /**
     * gets the report id
     */
    public static int getReportId(){
        return reportId;
    }

    /**
     * Sets the report id 
     */
    public void setReportId(int reportId){
        this.reportId = reportId;
    }
}

