public class playTest {

    public static void main (String args[]){
        String command = "$play https://www.youtube.com/watch?v=aV2Dfcz20xQ";
        String[] message = command.split(" ");
        System.out.println(message.length);
        String music = "";

        if(message.length != 2) {
            for (int i = 1; i < message.length; i++) {
                music += message[i] + " ";
            }
        } else music = message[1];



        System.out.println(music);
    }


}
