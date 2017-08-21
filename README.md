Building
---

Clone this repository and run `mvn package shade:shade`

Configuration
---

Copy `config.template.json` to `config.json` and update the Factorio game path

JSON Data Extraction
---

Run `java -cp target/FactorioDataWrapper-0.0.1-SNAPSHOT.jar com.demod.factorio.JsonExtractor` which will generate `output.json`