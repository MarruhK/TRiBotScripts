package scripts.gengarlibrary;

public class GString
{
    /**
     * Tribot API treat spaces as non breaking spaces (char code 160, unicode 00A0, html &nbsp;) instead of the regular
     * space (char code 32, unicode 0020).
     *
     * This method will convert a String with char code 32 spaces into the tribot string for comparison sake.
     *
     * @return
     */
    public static String convertToTribotString(String original)
    {
        if (original == null)
            return null;

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < original.length(); i++)
        {
            if (original.charAt(i) != 32)
            {
                s.append(original.charAt(i));
            }
            else
            {
                char c = 160;
                s.append(c);
            }
        }

        return s.toString();
    }
}
