package pluto.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetOpt {

    // common constants
    public static String NO_MATCH = "__NO_MATCH__";

    private static String INTERNAL_NEXT_VALUE = "__NEXT_VALUE__";

    // for validating short option names
    private static String SHORT_OPT_NAME_PATTERN = "^(\\p{Alpha}:?)+$";

    // for validate long option names
    private static String LONG_OPT_NAME_PATTERN = "^[\\p{Alnum}|-|_]++$";

    // pattern to scan long options
    private static String LONG_OPT_PATTERN_FORMAT = "^(--%s)$";
    private static String LONG_OPT_VALUE_PATTERN_FORMAT = "^(--%s)=(.++)$";

    private String mShortOpts;
    private List<Pattern> mLongOptPatterns;

    // constructor, process option definitions
    public GetOpt(String shortOpts, Collection<String> longOpts) {

        validateShortOptions(shortOpts);
        processLongOptions(longOpts);
    }

    /**
     *  process arguments, find out options/values
     * @param args
     * @return matching option or option/value pairs
     */
    public List getOpt(String[] args) {

        List<Object> result = new ArrayList<Object>();
        List<String> notOptions = new ArrayList<String>();

        for(String arg : args) {

            // process __NEXT_VALUE__ from last short list run
            if(result.contains(INTERNAL_NEXT_VALUE)) {
                // remove the special flag
                result.remove(INTERNAL_NEXT_VALUE);

                // this "arg" should be the value of last short option,
                // so replace the last entry with value added
                Object last = result.get(result.size()-1);
                result.remove(last);

                assert(last instanceof List);
                result.add(Arrays.asList(((List)last).get(0), arg));

                // current arg is the value of last short option, process is done here
                continue;
            }

            if(arg.startsWith("--")) {
                result.add(getLongOption(arg));
            }
            else if(arg.startsWith("-")) {
                result.addAll(getShortOptions(arg));
            }
            else {
                notOptions.add(arg);
            }
        }

        if(result.contains(INTERNAL_NEXT_VALUE)) {
            // next value is not replaced, wrong args
            throw new RuntimeException(String.format("Short option %s value is missing", result.get(result.size()-2)));
        }

        if(notOptions.size() > 0) {
            result.add(Arrays.asList(NO_MATCH, notOptions.toArray()));
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////
    // private methods

    /*
     * process short options
     */
    private void validateShortOptions(String shortOpts) {

        if(shortOpts == null || shortOpts.length() == 0) {
            // no short options defined
            return;
        }

        // validate the short pattern string
        if(!shortOpts.matches(SHORT_OPT_NAME_PATTERN)) {
            throw new RuntimeException(String.format("Invalid Short Options \"%s\"", shortOpts));
        }

        mShortOpts = shortOpts;
    }

    /*
     * process long options
     */
    private void processLongOptions(Collection<String> longOpts) {

        mLongOptPatterns = new ArrayList<Pattern>();
        if(longOpts != null && longOpts.size() > 0) {
            for(String opt : longOpts) {
                assert(opt.length() > 0);

                if(opt.endsWith("=")) {
                    // long option with value
                    final String optName = opt.substring(0, opt.length()-1);

                    // make sure option name is valid
                    assert(Pattern.compile(LONG_OPT_NAME_PATTERN).matcher(optName).matches()) : String.format("Invalid Long Options \"%s\"", longOpts);

                    final Pattern optPattern = Pattern.compile(String.format(LONG_OPT_VALUE_PATTERN_FORMAT, optName));
                    mLongOptPatterns.add(optPattern);
                }
                else {
                    // long option without value
                    assert(Pattern.compile(LONG_OPT_NAME_PATTERN).matcher(opt).matches()) : String.format("Invalid Long Options \"%s\"", longOpts);

                    final Pattern optPattern = Pattern.compile(String.format(LONG_OPT_PATTERN_FORMAT, opt));
                    mLongOptPatterns.add(optPattern);
                }
            }
        }
    }

    // arg must start with "-"
    private List getShortOptions(String arg) {

        List result = new ArrayList();

        if(mShortOpts == null || mShortOpts.length() == 0) {
            throw new RuntimeException(String.format("No Short Option Defined, Wrong Option %s", arg));
        }

        // for short options, we are not using pattern, just scan the string, ignore leading "-"
        final int len = arg.length();
        for (int i = 1; i < len; i++) {
            String opt = String.valueOf(arg.charAt(i));

            if(i == len - 1) {
                // this is the last letter in short option string, such as "c" in "abc"
                // this is the only one can have value
                if(isValidShortOptionWithValue(opt)) {

                    result.add(Arrays.asList("-" + opt));
                    result.add(INTERNAL_NEXT_VALUE);

                    // this is the last option, just break out
                    break;
                }
            }

            if(isValidShortOptionWithoutValue(opt)) {
                // short option is valid
                result.add(Arrays.asList("-" + opt));
            }
        }

        return result;
    }

    private boolean isValidShortOptionWithoutValue(String opt) {
        if(mShortOpts != null && mShortOpts.length() > 0) {
            return (mShortOpts.indexOf(opt) >= 0 && mShortOpts.indexOf(opt + ":") < 0);
        }

        return false;
    }

    private boolean isValidShortOptionWithValue(String opt) {
        if(mShortOpts != null && mShortOpts.length() > 0) {
            return (mShortOpts.indexOf(opt + ":") >= 0);
        }

        return false;
    }

    // arg must start with "--"
    private List<String> getLongOption(String arg) {

        // go through all patters to find a match
        for(Pattern pattern : mLongOptPatterns) {
            final Matcher matcher = pattern.matcher(arg);
            if(matcher.matches()) {
                final String name = matcher.group(1);

                String value = null;
                if(matcher.groupCount() > 1) {
                    value = matcher.group(2);
                }

                // add matched arg name/value pair to result list
                return Arrays.asList(name, value);
            }
        }

        // no match found, wrong option
        throw new RuntimeException(String.format("Unknown Option %s", arg));
    }
}

