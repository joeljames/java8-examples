package examples;

public static void main(String[]args){
        /*
        JFR is a tool that collects events in JVM during execution of application

        Use the below jcmd to capture the JFR binary(Java 11 and above):
            jcmd 1 JFR.start settings=profile name=jfr_name duration=2m filename=/tmp/jfr_name.jfr

         Wait the ~2 minutes for it to run, then:
            exit

         To visualize install Azul Mission Control https://docs.azul.com/azul-mission-control/install
         Download the respective tar file.

         Un zip the package
         mkdir ~/mission-control
         mv <zmc_package> ~/mission-control

         Add alias in .zshrc
         alias amc="$HOME/mission-control/zmc8.3.1.81-ca-macos_aarch64/Azul\ Mission\ Control.app/Contents/MacOS/zmc"

         Start the Azul mission control.
         Open a new terminal window.
         amc

         Now, you can open the jfr by foing to files -> open

        */

}