/**
 * Deeply inspired from https://chrismorgan.info/blog/dark-theme-implementation/
 *
 * Script must be included in body to avoid flickering
 * */

let prefName = 'dark';
let themerButton = document.getElementById('themer');
let enabled;

function switcher(offLabel, onLabel, defaultValue, apply) {

    function sync() {
        apply(enabled);
        // Update the button label
        themerButton.childNodes[1].data = enabled ? offLabel : onLabel;
        themerButton.title = 'Switch to ' + themerButton.childNodes[1].data + ' theme';
        // Try to persist to storage, if we’re effecting a deliberate change
        try {
            if (localStorage.getItem(prefName) || enabled != defaultValue) {
                localStorage.setItem(prefName, enabled);
            }
        } catch (e) {
        }
    }

    themerButton.addEventListener('click', () => {
        enabled = +!enabled;
        sync();
    });
    try {
        enabled = localStorage.getItem(prefName);
    } catch (e) {
    }
    // enabled should now be null, '0' or '1'. Fill in the default value,
    // coerce what may be something like '0', '1', true or false to 0 or 1,
    // and act on it.
    enabled = +(enabled || defaultValue);
    sync();
    return sync;
}

let isDarkModeDefault = 0;
const matcher = window.matchMedia('(prefers-color-scheme: dark)');
isDarkModeDefault = matcher.matches;
matcher.addEventListener('change', onPreferredSchemeChange);

let stylesheetElements = document.querySelectorAll(
    '[media="screen and (prefers-color-scheme: dark)"]'
);
let isThemerButtonVisible = stylesheetElements.length > 0;
setTimeout(function() {
    isThemerButtonVisible = stylesheetElements.length > 0;
    //console.log("isThemerButtonVisible: " + isThemerButtonVisible);
    themerButton.style.display = isThemerButtonVisible ? '' : 'none';
}, 100);

const syncDarkMode = switcher(
     'light', 'dark', isDarkModeDefault, enabled => {
        let colorScheme = document.querySelector('meta[name=color-scheme]');
        if(colorScheme != null){
            colorScheme.content = enabled ? 'dark' : 'light';
        }
        stylesheetElements.forEach(element => {
            element.media = enabled ? 'screen' : 'not all';
        });
    });

/**
 * On Preferred Scheme Change, deleted former setting and set the one from system
 */
function onPreferredSchemeChange(){
    isDarkModeDefault = matcher.matches;
    if(isDarkModeDefault == enabled){
        return;
    }
    enabled = isDarkModeDefault;
    localStorage.removeItem(prefName);
    //console.log("syncDarkMode from onPreferredSchemeChange");
    syncDarkMode();
}

// If anything is in the source after this script, handle it.
addEventListener('DOMContentLoaded', () => {
    stylesheetElements = [
        ...stylesheetElements,
        ...document.querySelectorAll(
            '[media="screen and (prefers-color-scheme: dark)"]'
        ),
    ];
    //console.log("syncDarkMode from DOMContentLoaded");
    syncDarkMode();
});