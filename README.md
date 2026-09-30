# Memory game

Memory is a popular board game consisting of a set of matching cards with matching images. The main goal is to find all the pairs with a minimum of mistakes. Memory games develop visual memory, attention, and concentration. This game uses cards with fantasy-themed pictures.

<p align="center">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo1.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo2.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo3.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo4.png" hspace="10" width="180" title="UI">
</p>

<p align="center">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo5.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo7.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo6.png" hspace="10" width="180" title="UI">
  <img src="https://github.com/alexrnov/Files/blob/master/fantazy_memo8.png" hspace="10" width="180" title="UI">
</p>

Vertex-shader example:

```glsl
#version 300 es
precision lowp float;
uniform mat4 u_mvpMatrix;
uniform mat4 u_mvMatrix;
uniform mat4 u_pointViewMatrix;

in vec4 a_position;
in vec2 a_textureCoordinates;
in vec3 a_normal;

out vec2 v_textureCoordinates;

struct AmbientLight {
    vec3 color;
    float intensity;
};

struct DiffuseLight {
    vec3 color;
    float intensity;
};

uniform AmbientLight u_ambientLight;
uniform DiffuseLight u_diffuseLight;

const vec3 lightPosition = vec3(0.0, 0.0, 18.0);

void main() {
    lowp vec3 ambientColor = u_ambientLight.color * u_ambientLight.intensity;
    
    vec3 modelViewNormal = vec3(u_mvMatrix * vec4(a_normal, 0.0));
    vec3 modelViewVertex = vec3(u_mvMatrix * a_position); // eye coord
    vec3 lightVector = normalize(lightPosition - modelViewVertex);
    lightVector = mat3(u_pointViewMatrix) * lightVector;
    float diffuse = max(dot(modelViewNormal, lightVector), 0.0);

    lowp vec3 diffuseColor = diffuse * u_diffuseLight.color * u_diffuseLight.intensity;

    v_commonLight = vec4((ambientColor + diffuseColor), 1.0);
    v_textureCoordinates = a_textureCoordinates;
    gl_Position = u_mvpMatrix * a_position;
}
```

Room example: 

```java
@Dao
public interface FavoritesRequests {
	@Query("SELECT COUNT(*) FROM FavoriteEntity")
	int getCountFavorites();

	@Query("SELECT * FROM FavoriteEntity")
	List<FavoriteEntity> getAll();

	@Query("SELECT id FROM FavoriteEntity ORDER BY id DESC LIMIT 1")
	long getLastCardId();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(FavoriteEntity favoriteEntity);

	@Query("SELECT EXISTS(SELECT 1 FROM FavoriteEntity WHERE path = :path LIMIT 1)")
	boolean isPathExists(String path);

	@Query("DELETE FROM FavoriteEntity WHERE path = :path")
	void deleteByPath(String path);
}
```
