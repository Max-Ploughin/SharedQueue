# Shared Queue (Readers and Writers)
___
A shared queue has been implemented to be accessed by multiple readers and a single writer. The queue handles the reception and release of string objects. A test program has been provided to demonstrate the usage of the queue. The sample program creates five threads responsible for consuming strings from the queue, along with a single writer that adds strings to the queue. The writer adds five messages per second, and the distribution of messages among consumers is relatively even.

It is important to note that neither the consumers nor the queue's functionality facilitating message consumption relies on Thread.sleep(), SLEEP(3), or similar calls.