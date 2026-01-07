# Java Concurrency & Parallelism Assignment

- Question 1: Producer–Consumer using `synchronized`, `wait()`, `notifyAll()`
- Question 2: Producer–Consumer using `ExecutorService`, `ReentrantLock`, `Condition`
- Question 3: Callable & Future (Blocking Behavior)

---

## Question 1  
### Producer–Consumer using `synchronized`, `wait()`, `notifyAll()`

### Objective
To implement the classic **Producer–Consumer problem** using **intrinsic locking** and **inter-thread communication**.

---

### Description
- A shared `MessageQueue` stores messages.
- Multiple producer threads add messages to the queue.
- Multiple consumer threads remove messages from the queue.
- If the queue is empty, consumers wait.
- When a producer adds a message, waiting consumers are notified.

---

### How It Works
1. Producers call `put()` to add messages.
2. Consumers call `take()` to remove messages.
3. If the queue is empty:
   - Consumer calls `wait()` and releases the lock.
4. When a producer adds a message:
   - It calls `notifyAll()` to wake waiting consumers.
5. Only one thread can access the queue at a time.

<img width="1041" height="513" alt="image" src="https://github.com/user-attachments/assets/8339e6c1-775d-43e0-a5e9-5ae7bf164c46" />


---

## Question 2  
### Producer–Consumer using Executors & Explicit Locks

### Objective
To refactor Question 1 using **modern Java concurrency tools** for better scalability and control.

---

### Description
- Uses `ExecutorService` to manage threads instead of manual thread creation.
- Uses `ReentrantLock` for explicit locking.
- Uses `Condition` to manage waiting and signaling.
- Separate thread pools are used for producers and consumers.

---

### How It Works
1. Producers and consumers are submitted as tasks to executor services.
2. `ReentrantLock` ensures only one thread accesses the queue at a time.
3. If the queue is empty, consumers call `await()` and release the lock.
4. Producers add messages and call `signalAll()` to wake consumers.
5. Executors are shut down gracefully after all tasks complete.

<img width="1058" height="568" alt="image" src="https://github.com/user-attachments/assets/a92e7020-63a2-4889-badd-9080f5295408" />


---

## Question 3 — Callable/Future + Streams (Sequential vs Parallel)

This folder/program contains **Question 3**, which has **two parts**:

- **Part A:** `Callable<Integer>` + `Future.get()` to observe **blocking behavior**
- **Part B:** Sum of **1,000,000 integers** using **sequential stream** and **parallel stream**, and compare execution time

---

## Part A — Callable & Future (Blocking Behavior)

### Objective
- Implement a `Callable<Integer>` that computes the sum from **1 to N**
- Submit it to an `ExecutorService`
- Retrieve the result using `Future.get()`
- Observe that `Future.get()` **blocks** (waits) until the task finishes
- Shutdown the executor properly

### How it works
1. A `Callable` task is created (it returns an `Integer` result).
2. The task is submitted to the executor using `submit()`.
3. `submit()` returns a `Future<Integer>`.
4. When `future.get()` is called:
   - If the task is still running, the **main thread stops and waits** (blocked).
   - When the task completes, `get()` returns the computed sum.
5. Executor is shut down using `shutdown()`.

<img width="541" height="185" alt="image" src="https://github.com/user-attachments/assets/53528633-3518-4eaf-ba03-0eb194e2cc6b" />


---

## ✅ Part B — Sequential Stream vs Parallel Stream

### Objective
- Generate a list (or range) of **1,000,000 integers**
- Compute the sum using:
  1. **Sequential stream**
  2. **Parallel stream**
- Print execution time for both
- Observe differences in execution time and output behavior

### How it works
1. Data is generated (1 to 1,000,000).
2. Sequential sum is computed using:
   - `stream().mapToLong(...).sum()`
3. Parallel sum is computed using:
   - `parallelStream().mapToLong(...).sum()`
4. Execution time is measured using `System.nanoTime()`.

### Observations
- The **sum is same** in both cases.
- The **time may differ**.
- Parallel stream is **slower**

<img width="642" height="112" alt="image" src="https://github.com/user-attachments/assets/17c7ea12-764f-49ca-ad8e-74cdd16d6219" />



