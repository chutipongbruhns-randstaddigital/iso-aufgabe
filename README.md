# iso-aufgabe
Ein kleines Beispiel Projekt mit folgender Aufgabenstellung:

1. Erstellen Sie ein Java-Spring-Boot-Projekt, das mithilfe von OpenAPI einen einfachen REST-Endpoint bereitstellt.
2. Persistieren Sie Daten mithilfe von Hibernate (JPA) in eine Datenbank.
3. Richten Sie eine einfache Jenkins-Pipeline ein, die
den Code aus einem Git-Repository checkt,
das Projekt baut und testet (Maven), und
einen kurzen Code-Quality-Check (welche) durchführt.
4. Dokumentieren Sie kurz, wie das Projekt aufgebaut ist und wie Sie die Pipeline konfiguriert haben.




---

## 1. Voraussetzungen (Lokale Installation)

Für dieses Projekt sind unterschiedliche Werkzeuge erforderlich, je nachdem, ob nur die App oder die gesamte Pipeline ausgeführt werden soll.

### 1.1. Voraussetzungen (Nur Applikation)

Um die Spring Boot-Anwendung lokal zu starten, wird Folgendes benötigt:
* **Git:** Zum Klonen des Repositorys.
* **Java JDK 21:** Das Projekt ist mit Java 21 konfiguriert.
* **Maven:** Das Projekt verwendet den Maven Wrapper (`mvnw`), sodass keine separate Maven-Installation erforderlich ist.

### 1.2. Voraussetzungen (Vollständige Pipeline)

Um die gesamte CI/CD-Pipeline (Jenkins & SonarQube) lokal auszuführen, sind zusätzlich zwingend erforderlich:
* **Docker Desktop:** (oder eine äquivalente Docker-Umgebung). Sowohl Jenkins als auch SonarQube werden als Docker-Container ausgeführt.

---

## 2. Ausführung (Nur Applikation)

So startest du die Spring Boot-Anwendung alleinstehend:

1.  Repository klonen:
    ```bash
    git clone [IHRE_GIT_REPO_URL]
    cd iso-aufgabe
    ```
2.  Anwendung mit dem Maven Wrapper starten:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  Die Anwendung ist verfügbar unter:
    * **Swagger UI (OpenAPI):** `http://localhost:8080/`.
    * **H2 Datenbank Konsole:** `http://localhost:8080/h2-console`.
        * Stelle sicher, dass die **JDBC URL** auf `jdbc:h2:mem:testdb` eingestellt ist, um die In-Memory-Datenbank zu sehen.

---

## 3. Ausführung (Vollständige CI/CD-Pipeline)

Dies ist die vollständige Simulation des DevOps-Zyklus.

### Schritt 1: Infrastruktur starten (Docker)

Wir müssen die beiden Server (SonarQube und Jenkins) in Docker-Containern starten.

1.  **SonarQube starten:**
    ```bash
    docker run -d -p 9000:9000 -v sonarqube_data:/opt/sonarqube/data sonarqube:lts-community
    ```
    (Warte 1-2 Minuten, bis der Server unter `http://localhost:9000` verfügbar ist. Login: `admin`/`admin`).

2.  **Jenkins starten:**
    ```bash
    docker run -d -p 9090:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts
    ```
    (Jenkins wird auf Port `9090` ausgeführt, um Konflikte zu vermeiden).

### Schritt 2: Jenkins Ersteinrichtung (Einmalig)

1.  Öffne Jenkins: `http://localhost:9090`.
2.  Hole das Admin-Passwort aus dem Docker-Log:
    ```bash
    docker exec [JENKINS_CONTAINER_ID] cat /var/jenkins_home/secrets/initialAdminPassword
    ```
3.  Folge dem Assistenten und wähle **"Install suggested plugins"**. Stelle sicher, dass "Git", "Pipeline" und "SonarQube Scanner for Jenkins" installiert werden (letzteres muss evtl. manuell hinzugefügt werden).

### Schritt 3: Jenkins & SonarQube verbinden (Einmalig)

1.  **SonarQube-Token generieren:**
    * Gehe zu SonarQube (`http://localhost:9000`).
    * Logge dich ein (admin/admin).
    * Gehe zu Administration (oder My Account) -> Security -> Generate Token (z.B. `jenkins-token`).
    * **Kopiere den Token.**

2.  **Jenkins System konfigurieren:**
    * Gehe zu Jenkins (`http://localhost:9090`).
    * Gehe zu **Jenkins verwalten** -> **System konfigurieren**.
    * **Jenkins Location:** Setze die **Jenkins URL** auf: `http://host.docker.internal:9090/` (Dies ist entscheidend für die Webhook-Callbacks).
    * **SonarQube servers:**
        * Klicke auf "Add SonarQube".
        * **Name:** `IsoAppSonarQube` (Der Name muss exakt mit dem in der `Jenkinsfile` übereinstimmen).
        * **Server URL:** `http://host.docker.internal:9000` (Dies ist die "magische" Docker-Adresse für den Host-PC).
        * **Server authentication token:** Klicke "Add" -> "Jenkins". Wähle als Art (Kind) **"Secret text"**. Füge den Token aus Schritt 3.1 ein. Wähle dieses neue Credential im Dropdown aus.
    * Speichere die Konfiguration.

3.  **SonarQube Webhook konfigurieren:**
    * Gehe zurück zu SonarQube (`http://localhost:9000`).
    * Gehe zu **Administration** -> **Configuration** -> **Webhooks**.
    * Klicke **"Create"**.
    * **Name:** `Jenkins-Callback`
    * **URL:** `http://host.docker.internal:9090/sonarqube-webhook/` (Achte auf den Schrägstrich am Ende).
    * Klicke "Create".

### Schritt 4: Jenkins Pipeline Job erstellen

1.  Gehe zum Jenkins Dashboard (`http://localhost:9090`).
2.  Klicke **"Neues Element"** -> Wähle **"Pipeline"** und gib einen Namen ein.
3.  Im Konfigurations-Tab "Pipeline", wähle:
    * **Definition:** `Pipeline script from SCM`.
    * **SCM:** `Git`.
    * **Repository URL:** Die URL zu deinem Git-Repository.
4.  Klicke **"Speichern"**.

### Schritt 5: Pipeline ausführen

Klicke auf **"Jetzt bauen"**.