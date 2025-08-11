# NodeJS Mobile

Bridge entre **Java (Android)** e **Node.js** para troca de mensagens e JSON em tempo real.  
Suporta detecção automática de tipo (texto, JSON Array, JSON Object) e callbacks customizáveis.

---

## Instalação via JitPack

### 1️⃣ Adicione o repositório JitPack no `settings.gradle` ou `build.gradle` de nível de projeto:
```gradle
maven { url 'https://jitpack.io' }
```

### 2️⃣ Adicione a dependência no `build.gradle` do módulo:
```gradle
dependencies {
    implementation 'com.github.devicewhite:NodeJS_Mobile:1.0.0'
}
```

[![](https://jitpack.io/v/devicewhite/NodeJS_Mobile.svg)](https://jitpack.io/#devicewhite/NodeJS_Mobile)

---

## Uso

### Inicializando
```java
NodeJS node = new NodeJS();
node.runCode("console.log('Hello from Node.js')");
```

---

### Executando um arquivo `.js`
```java
File file = new File(getFilesDir(), "script.js");
node.runFile(file);
```

---

### Enviando mensagem de texto
```java
node.sendMessage("Olá do Java!");
```

---

### Enviando JSON
```java
JSONObject obj = new JSONObject();
obj.put("name", "Mestre");
obj.put("lang", "Java");

node.sendJSON(obj);
```

---

### Recebendo mensagens no Android
```java
NodeJS node = new NodeJS(new NodeJS.OnNodeListener() {
    @Override
    public void onOutput(String message) {
        Log.d("Node Output", message);
    }

    @Override
    public void onMessage(int type, String rawText, JSONArray array, JSONObject object) {
        switch (type) {
            case MESSAGE_TYPE_RAW_TEXT:
                Log.d("RAW", rawText);
                break;
            case MESSAGE_TYPE_JSON_ARRAY:
                Log.d("ARRAY", array.toString());
                break;
            case MESSAGE_TYPE_JSON_OBJECT:
                Log.d("OBJECT", object.toString());
                break;
        }
    }
});
```
