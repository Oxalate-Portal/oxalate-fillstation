const fs = require('fs');
const path = require('path');
const {execSync} = require('child_process');
const dayjs = require("dayjs");

function run(command) {
    return execSync(command, {stdio: ['ignore', 'pipe', 'pipe']})
        .toString()
        .trim();
}

function gitFetch() {
    try {
        execSync('git fetch --tags --force', {stdio: 'inherit'});
    } catch (error) {
        console.warn('Warning: failed to fetch tags, continuing with local tags only.');
    }
}

function getGitTags() {
    try {
        const raw = run('git tag --list');
        return raw ? raw.split('\n')
                        .map((tag) => tag.trim())
                        .filter(Boolean) : [];
    } catch (error) {
        return [];
    }
}

function resolveNextPatchVersion(currentVersion, tags) {
    const [currentMajor, currentMinor] = currentVersion.split('.')
                                                       .map(Number);
    if (Number.isNaN(currentMajor) || Number.isNaN(currentMinor)) {
        throw new Error(`Invalid VERSION format '${currentVersion}'. Expected <major>.<minor>.`);
    }

    const versionPattern = new RegExp(`^v?${currentMajor}\\.${currentMinor}\\.(\\d+)$`);
    const matchingPatches = tags
        .map((tag) => versionPattern.exec(tag))
        .filter(Boolean)
        .map((match) => Number(match[1]))
        .filter((patch) => Number.isInteger(patch) && patch >= 0);

    const nextPatch = matchingPatches.length === 0 ? 0 : Math.max(...matchingPatches) + 1;
    return `${currentMajor}.${currentMinor}.${nextPatch}`;
}

function readBaseVersionFromFile() {
    const versionFilePath = path.resolve(__dirname, '../VERSION');
    if (!fs.existsSync(versionFilePath)) {
        throw new Error('VERSION file not found and BUILD_VERSION was not provided.');
    }
    return fs.readFileSync(versionFilePath, 'utf8')
             .trim();
}

function resolveVersion() {
    const overrideVersion = process.env.BUILD_VERSION;
    if (overrideVersion && overrideVersion.trim()) {
        return overrideVersion.trim();
    }

    const currentVersion = readBaseVersionFromFile();
    gitFetch();
    const tags = getGitTags();
    return resolveNextPatchVersion(currentVersion, tags);
}

const nextVersion = resolveVersion();

const buildTime = new Date().toISOString();
const buildTimeFormatted = dayjs(buildTime)
    .format("YYYY.MM.DD HH:mm")
const outputFilePath = path.resolve(__dirname, 'src/buildInfo.json');

fs.writeFileSync(outputFilePath, JSON.stringify({buildTime: buildTimeFormatted, version: nextVersion}), 'utf8');

console.log("==================================================================================");
console.log("Generating build time and version...");
console.log(`Build time: ${buildTimeFormatted}`);
console.log(`Next version: ${nextVersion}`);
console.log("==================================================================================");
