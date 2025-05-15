export default function capitalizeFirstLetter(input) {
    let lowerCased = input.toLowerCase();
    let capitalized = lowerCased.charAt(0).toUpperCase() + lowerCased.slice(1);
    return capitalized;
}