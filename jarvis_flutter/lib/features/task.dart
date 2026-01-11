class Task {
  static int _nextId = 0;
  final int id;
  final String description;
  bool isCompleted;

  Task(this.description)
      : id = _nextId++,
        isCompleted = false;

  Task._(this.id, this.description, this.isCompleted);

  factory Task.fromJson(Map<String, dynamic> json) {
    return Task._(
      json['id'],
      json['description'],
      json['isCompleted'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'description': description,
      'isCompleted': isCompleted,
    };
  }

  static void setNextId(int id) {
    _nextId = id;
  }
}
