package itm.two;

public class QueueElement {
    public int i;
    public String forOutput;

    /**
     * 1 - checked for fizziness
     * 2 - checked for buzziness
     * 4 - found to be either fizzy or buzzy
     * 8 - checked for fizzbuzziness
     * 16 - printed out
     */
    public byte flags = 0;

    QueueElement(int i) {
        this.i = i;
        forOutput = String.valueOf(i);
    }
}
