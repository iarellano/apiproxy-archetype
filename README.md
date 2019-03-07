# API Proxy maven archetype

## Setup

### Clone the repo
```bash
git clone git@github.com:iarellano/apiproxy-archetype.git
```

### Change to repo directory
```bash
cd apiproxy-archetype
```

### Install archetype to local repo
```bash
mvn clean install 
```

## Create your first API Proxy project

In you projects' directory execute the archetype with your own parameters
```bash
mvn archetype:generate                                  \
  -DarchetypeGroupId=com.github.iarellano               \
  -DarchetypeArtifactId=single-apiproxy-archetype       \
  -DarchetypeVersion=1.0-SNAPSHOT                       \
  -DgroupId=[target group id]                           \
  -DartifactId=[target artefact id]                     \
  -DtestOrganization=[your edge organization]
```

A directory named after your ```artefactId``` is created, form now on you can edit your project with your favorite IDE.
