
## JFR (Java Flight Recorder)

1. Use the below jcmd to capture the JFR binary(Java 11 and above):
    jcmd 1 JFR.start settings=profile name=jfr_name duration=2m filename=/tmp/jfr_name.jfr

2. Wait the ~2 minutes for it to run, then:
    exit

3. To visualize install Azul Mission Control https://docs.azul.com/azul-mission-control/install
    Download the respective tar file.

4. Un-zip the package
    mkdir ~/mission-control 
    mv <zmc_package> ~/mission-control

5. Add alias in .zshrc
    alias amc="$HOME/mission-control/zmc8.3.1.81-ca-macos_aarch64/Azul\ Mission\ Control.app/Contents/MacOS/zmc"

6. Start the Azul mission control.
         Open a new terminal window.
         amc
7. Now, you can open the jfr by foing to files -> open
