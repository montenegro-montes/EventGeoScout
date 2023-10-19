library(XML)
library(raster)
library(rgeos)
library(rgdal)
library(geosphere)
library(sf)
library(leaflet)
library(leaflet.extras)
library(mapview) # save map as png


setwd("/Users/joseamontenegromontes/Desktop/marathon")
gpx_file <-"./gpx/malaga22.gpx"

load_gpx <-function(){

  #LOAD GPX
  pfile <- htmlTreeParse(gpx_file, error = function (...) {}, useInternalNodes = T)
  coords <- xpathSApply(pfile, path = "//trkpt", xmlAttrs)
  
  lats <- as.numeric(coords["lat",])
  lons <- as.numeric(coords["lon",])
  
  df <- data.frame(
    lats = as.numeric(coords["lat", ]),
    lons = as.numeric(coords["lon", ])
  )
  
  #PRINT Recorrido Marathon
  map<-leaflet() %>%
    addTiles() %>%
    addPolylines(data = df, lat = ~lats, lng = ~lons, color = "blue", opacity = 0.6, weight = 3)
  mapshot(map, file="RecorridoMarathonMalaga.png", cliprect = "viewport")
  
  marathon<-SpatialLines(list(Lines(Line(cbind(lons,lats)), ID="marathon")))
  
  proj4string(marathon) <- CRS(SRS_string = "epsg:4326")
  pc <- spTransform( marathon,  CRS("+proj=utm +zone=30 +datum=WGS84"))
  
  numOfPoints  <-  gLength(pc)
  
  points <- spsample(pc, n = numOfPoints, type = "regular");
  spgeo  <- spTransform(points, CRS("+proj=longlat +datum=WGS84"))  
  
  return(spgeo)
}

load_corredores<-function(csv){
  #LOAD CORREDORES
  corredores <- read.csv(file=csv, header=FALSE, sep=";", dec=",")
  
  return (corredores)
}
  
aux_km <-function (columna){
  km <- switch(columna,
               "1" = "Km5",
               "2" = "Km10",
               "3" = "Km20",
               "4" = "Km30",
               "5" = "Km38", 
               "6" = "Fin",
               "7" = "Fin+1m",
               "8" = "Fin+10m",
               "9" = "Fin+20m",
               "10" = "Fin+30m",
               "11" = "Fin+40m", 
               "12" = "Fin+1h",
               "13" = "Fin+1,5h",
               "14" = "Fin+2h",
               "15" = "Fin+2,5",
               "16" = "Fin+3h",
               "17" = "Fin+3,5h",
               "default" = "Err"
  )
  return (km)
}

heatMap_corredores<-function(corredores,spgeo,columna,year){
 
  km <- aux_km(columna)
  fileMap=sprintf("./HeatMap/HeatMap%d/HeatMap%s-%d.png",year,km,year)

  log<-corredores[,columna]
  log<-log[!is.na(log)] 
  
  longV <- c()
  latV <- c() 
  indice = 0
  
  for (i in log){
    longV[indice] <- spgeo[i]@coords[1]
    latV[indice] <- spgeo[i]@coords[2]
    indice<-indice+1
  }
  
  map <- leaflet(corredores) %>%
    addTiles() %>%
    addHeatmap( lat = ~latV, lng = ~longV, max=.6, blur = 60)%>%
    addCircles( lng = ~longV,  lat = ~latV,color = "black",weight =0.5)
 
  mapshot(map, file=fileMap, cliprect = "viewport")
  
}

heatMap_ggplot_corredores<-function(corredores,spgeo,columna,year){
  
  km <- aux_km(columna)
  
  fileMap=sprintf("./HeatMap/HeatMap%d/HeatMapColor%s-%d.png",year,km,year)
  
  
  log<-corredores[,columna]
  log<-log[!is.na(log)] 
  
  longV <- c()
  latV <- c() 
  indice = 0
  
  for (i in log){
    longV[indice] <- spgeo[i]@coords[1]
    latV[indice] <- spgeo[i]@coords[2]
    indice<-indice+1
  }
  
  corredor <- data.frame(lat=latV, long=longV)
  
  png(fileMap, width = 1000, height = 800, res = 100)
  
  map <- ggplot(corredor, aes(long, lat))
  map <- map + geom_bin2d() + scale_fill_gradientn(limits=c(0,50), breaks=seq(0, 50, by=10), colours=rainbow(4))
  map <- map + geom_point()
    
  print(map)
  dev.off()

}

aux_corredor <- function(corredores,spgeo,columna){

    log<-corredores[,columna]
    log<-log[!is.na(log)] 
    
    longV <- c()
    latV <- c() 
    indice=0
   
     for (i in log){
      longV[indice] <- spgeo[i]@coords[1]
      latV[indice] <- spgeo[i]@coords[2]
      indice<-indice+1
    }
    corredor <- data.frame(lat=latV, long=longV)
    
    return (corredor)
}  
  


write_geoson <- function (corredores,spgeo,year,columna){
  
    km <- aux_km(columna)
  
    fileMap=sprintf("./GeoJson/GeoJson%d/Corredores%s-%d.json",year,km,year)
    
    print(fileMap)
    
    corredor = aux_corredor (corredores,spgeo,columna)
    
    xy <- SpatialPointsDataFrame(
      matrix(c(corredor$long,corredor$lat), ncol=2), data.frame(ID=seq(1:length(corredor$long))),
      proj4string=CRS("+proj=longlat +ellps=WGS84 +datum=WGS84"))
    
    class(xy)
    layerValue=sprintf("corredores%s-%d.json",km,year)
    writeOGR(xy, fileMap, layer=layerValue, driver="GeoJSON")
}




###################
spgeo   <-  load_gpx()

years <- c(19, 21, 22)

for (year in years) {
  print(year)

  csv          <- sprintf("marathonMetrosMalaga%d.csv",year) 
  corredores   <-load_corredores(csv)
  num_columnas <- ncol(corredores)

  
  for (columna in 1:num_columnas) {
    heatMap_ggplot_corredores(corredores,spgeo,columna,year)
    write_geoson (corredores,spgeo,year,columna)
    heatMap_corredores(corredores,spgeo,columna,year)
  }
}





