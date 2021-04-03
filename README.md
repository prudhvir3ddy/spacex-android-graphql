# spacex-android-graphql
android app client for apollo graphql demo 

## Tech Used 
- [Kotlin](kotlinlang.org/) - primary language used to code the app
- [Apollo Android](https://github.com/apollographql/apollo-android) - network client for graphql api consuming
- [Paging 3 jetpack](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - network and memory efficient pagination
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - context switching and concurrency
- [Flows](https://kotlinlang.org/docs/flow.html) -  exposing data and operations from repository layer
- [Navigation component](https://developer.android.com/guide/navigation) -  navigating across app
- [Dagger hilt](https://dagger.dev/hilt/) -  dependency injection
- [SpaceX GraphQL API](https://github.com/spacexland/api)

## Useful tools when working with graphql schema generation
- https://apollo-fullstack-tutorial.herokuapp.com/

## Few things to do - future
- [ ] Add offline support to the app 
- [ ] show proper messages of errors from api instead of generic screen
- [ ] not call the api when sorting
