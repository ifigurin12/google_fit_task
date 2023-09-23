part of 'fitness_bloc.dart';

@immutable
sealed class FitnessState {}

final class FitnessInitial extends FitnessState {}


class FitnessDataLoadingState extends FitnessState {}

class FitnessDataSuccessState extends FitnessState {
  final fitnessData; 

  FitnessDataSuccessState({required this.fitnessData});
}

class FitnessDataFailureState extends FitnessState {}