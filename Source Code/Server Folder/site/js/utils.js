// BBG

export function signature() {
    console.log("BBG JS Script.");
}

export function inject(id, toInject) {
    // inject type: "string"
    // specializedInjectN(id, toInject);
    document.getElementById(id).innerHTML += toInject;
}

export function getValue(id) {
    // return type: "string"
    return document.getElementById(id).value;
}

export function setValue(id, value) {
    document.getElementById(id).value = value;
}

export function getValueRaw(id) {
    // return type: "string"
    return document.getElementById(id).innerHTML;
}

export function clear(id) {
    document.getElementById(id).innerHTML = null;
}

export function clearAndInject(id, toInject) {
    clear(id);
    inject(id, toInject);
}

export function resetForm(formId) {
    document.getElementById(formId).reset();
}

export function end() {
    console.log("Task compvared.");
}

export function specializedInjectN(id, toInject) {
    // console.log("ex caught by sIjN");
}

export function getRadiobuttonCheckState(id) {
    // return type: "boolean"
    return document.getElementById(id).checked;
}

export function setRadiobuttonCheckState(id, state) {
    document.getElementById(id).checked = state;
}

export function testStringEmpty(string) {
    // return type: "boolean"
    return string == null || string == "";
}

export function testDateValidity(dateString) {
    // return type: "boolean"
    var date = Date.parse(dateString);
    return date != null && date != "Invalid Date" && !Number.isNaN(date);
}

export function testDateValidityNow(dateString) {
    // return type: "boolean"
    if (testDateValidity(dateString)) {
        var now = new Date();
        var date = Date.parse(dateString);
        return !(date > now);
    }
    else {
        return false;
    }
}

/*
@deprecated
export function testStringNumber(string) {
    // return type: "boolean"
    var number = Number.parseInt(string);
    return number != null && number != "NaN";
}
*/

export function testStringContainsOnlyvarters(string) {
    // Regular Expression
    // A regular expression is an object that describes a pattern of characters.
    return /^[a-zA-Z]+$/.test(string);
}

export function testStringContainsOnlyNumbers(string) {
    // Regular Expression
    // A regular expression is an object that describes a pattern of characters.
    return /^\d+$/.test(string);
}

export function testStringNotEmptyContainsOnlyvarters(string) {
    return testStringEmpty(string) ? false : testStringContainsOnlyvarters(string);
}

export function testStringNotEmptyContainsOnlyNumbers(string) {
    return testStringEmpty(string) ? false : testStringContainsOnlyNumbers(string);
}

export function getAge(dateString) {
    if (testDateValidityNow(dateString)) {
        var now = new Date();
        var date = Date.parse(dateString);
        var age = Math.floor((now - date) / 3.154e+10);

        if (age < 10) {
            return age.toPrecision(1);
        } else if (age < 100) {
            return age.toPrecision(2);
        } else if (age <= 130) {
            return age.toPrecision(3);
        } else {
            return null;
        }
    }
    else {
        return null;
    }
}