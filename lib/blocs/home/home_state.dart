part of 'home_bloc.dart';

@immutable
abstract class HomeState {}

class HomeLoading extends HomeState {}

class HomeDataEmpty extends HomeState {}

class HomeDataLoaded extends HomeState {
  final List<dynamic> data;

  HomeDataLoaded(this.data);
}
